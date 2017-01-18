package com.soldiersofmobile.todoexpert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.TodoItem;
import com.soldiersofmobile.todoexpert.api.TodosResponse;
import com.soldiersofmobile.todoexpert.db.TodoDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class RefreshIntentService extends IntentService {

    public static final String ACTION = "com.soldiersofmobile.todoekspert.REFRESH_ACTION";
    public static final int NOTIFICATION_ID = 1;
    private TodoApi api;
    private LoginManager loginManager;
    private TodoDao todoDao;

    public RefreshIntentService() {
        super(RefreshIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("Refresh in service");
        TodoApplication application = (TodoApplication) getApplication();
        api = application.getTodoApi();
        loginManager = application.getLoginManager();
        todoDao = application.getTodoDao();

        Call<TodosResponse> call = api.getTodos(loginManager.getSessionToken());


        try {
            Response<TodosResponse> response = call.execute();
            if (response.isSuccessful()) {

                List<TodoItem> results = response.body().results;

                for (TodoItem todo : results) {
                    todoDao.create(todo, loginManager.getUserId());
                }
                sendBroadcast(new Intent(ACTION));

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                builder.setContentTitle("Todos updated");
                builder.setContentText("Todos:" + results.size());
                builder.setSmallIcon(R.mipmap.ic_launcher);

                Intent startActivityIntent = new Intent(this, TodoListActivity.class);
                startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, startActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                Notification notification = builder.build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_ID);
                notificationManager.notify(NOTIFICATION_ID, notification);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
