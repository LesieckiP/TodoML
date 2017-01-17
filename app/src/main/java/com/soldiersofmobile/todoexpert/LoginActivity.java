package com.soldiersofmobile.todoexpert;

import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
        AsyncTask<String, Integer, Boolean> asyncTask = new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginButton.setEnabled(false);
            }

            @Override
            protected void onPostExecute(final Boolean valid) {
                super.onPostExecute(valid);
                loginButton.setEnabled(true);
                if(valid) {
                    finish();
                }
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                loginButton.setText(String.valueOf(values[0]));
            }

            @Override
            protected Boolean doInBackground(final String... params) {
                for (int i = 0; i < 100; i++) {

                    try {
                        Thread.sleep(50);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return params[0].equals("testxx") && params[1].equals("testxx");
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
