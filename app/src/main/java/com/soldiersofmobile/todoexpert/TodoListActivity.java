package com.soldiersofmobile.todoexpert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.TodoItem;
import com.soldiersofmobile.todoexpert.api.TodosResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoListActivity extends AppCompatActivity {

    public static final String TODO_EXTRA = "todo";
    public static final int REQUEST_CODE = 123;
    private static final String TAG = TodoListActivity.class.getSimpleName();
    @BindView(R.id.todos_list) ListView todosList;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    private LoginManager loginManager;
    private TodoApi todoApi;
    private TodosAdapter todosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TodoApplication application = (TodoApplication) getApplication();
        loginManager = application.getLoginManager();
        todoApi = application.getTodoApi();

        if (loginManager.hasToLogin()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        todosAdapter = new TodosAdapter();
        todosList.setAdapter(todosAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodo();
            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadFromServer() {
        Call<TodosResponse> call = todoApi.getTodos(loginManager.getSessionToken());
        call.enqueue(new Callback<TodosResponse>() {
            @Override
            public void onResponse(
                    final Call<TodosResponse> call,
                    final Response<TodosResponse> response
            ) {

                if (response.isSuccessful()) {
                    for (TodoItem result : response.body().results) {
                        Log.d(TAG, result.toString());
                    }
                    todosAdapter.addAll(response.body().results);
                }
            }

            @Override
            public void onFailure(
                    final Call<TodosResponse> call,
                    final Throwable t
            ) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addTodo();
                break;
            case R.id.action_refresh:
                loadFromServer();
                break;
            case R.id.action_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.logout_dialog);
                builder.setMessage(R.string.are_you_sure);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            final DialogInterface dialog,
                            final int which
                    ) {

                        loginManager.logout();
                        goToLogin();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.setCancelable(false);
                builder.create().show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTodo() {
        Intent intent = new Intent(this, AddTodoActivity.class);
        //"edit"  intent.putExtra(TODO_EXTRA, new Todo("task", true));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Todo todo = (Todo) data.getParcelableExtra(TODO_EXTRA);
            Toast.makeText(this, "Result:" + resultCode + todo, Toast.LENGTH_SHORT).show();
        }
    }
}
