package com.soldiersofmobile.todoexpert;

import java.io.IOException;
import java.lang.annotation.Annotation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.UserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText) EditText usernameEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.loginButton) Button loginButton;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.loginButton, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                performLogin();
                break;
            case R.id.register_button:
                break;
        }
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValid = true;
        if (username.length() <= 5) {
            isValid = false;
            usernameEditText.setError(getString(R.string.username_too_short));
        }
        if (password.isEmpty()) {
            isValid = false;
            passwordEditText.setError(getString(R.string.password_empty_error));
        }

        if (isValid) {
            login(username, password);
        }
    }

    private void login(
            final String username,
            final String password
    ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                //TODO setup gson
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Converter<ResponseBody, ErrorResponse> converter
                = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        TodoApi todoApi = retrofit.create(TodoApi.class);
        Call<UserResponse> login = todoApi.login(username, password);

        login.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(
                    final Call<UserResponse> call,
                    final Response<UserResponse> response
            ) {

                if (response.isSuccessful()) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getSessionToken());
                    editor.putString("user_id", response.body().getObjectId());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, TodoListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Converter<ResponseBody, ErrorResponse> converter
                            = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    try {
                        ErrorResponse errorResponse = converter.convert(response.errorBody());
                        Toast.makeText(LoginActivity.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(
                    final Call<UserResponse> call,
                    final Throwable t
            ) {

                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginWithAsyncTask(
            final String username,
            final String password
    ) {
        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginButton.setEnabled(false);
            }

            @Override
            protected void onPostExecute(final String errorMessage) {
                super.onPostExecute(errorMessage);
                loginButton.setEnabled(true);
                if (errorMessage == null) {
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                loginButton.setText(String.valueOf(values[0]));
            }

            @Override
            protected String doInBackground(final String... params) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Gson gson = new GsonBuilder()
                        //TODO setup gson
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://parseapi.back4app.com")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Converter<ResponseBody, ErrorResponse> converter
                        = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                TodoApi todoApi = retrofit.create(TodoApi.class);
                Call<UserResponse> login = todoApi.login(params[0], params[1]);
                try {
                    Response<UserResponse> userResponse = login.execute();

                    if (userResponse.isSuccessful()) {
                        //TODO
                        return null;
                    } else {
                        ErrorResponse errorResponse = converter.convert(userResponse.errorBody());
                        return errorResponse.getError();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

            }
        };
        asyncTask.execute(username, password);
    }

    private void loginWithHandler(
            final String username,
            final String password
    ) {
        loginButton.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            loginButton.setEnabled(true);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
