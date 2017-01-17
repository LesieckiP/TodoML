package com.soldiersofmobile.todoexpert;

import java.io.IOException;

import android.content.SharedPreferences;

import com.soldiersofmobile.todoexpert.api.ErrorResponse;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginManager {

    public static final String SESSION_TOKEN = "sessionToken";
    public static final String USER_ID = "userId";
    public static final String USER = "user";

    public void logout() {
        userId = null;
        user = null;
        sessionToken = null;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SESSION_TOKEN);
        editor.remove(USER_ID);
        editor.remove(USER);
        editor.apply();
    }

    public interface LoginCallback {

        void setLoginEnabled(boolean enabled);

        void showErrorMessage(String message);

        void loginOk();
    }

    private LoginCallback loginCallback;
    private final TodoApi api;
    private final Converter<ResponseBody, ErrorResponse> converter;
    private final SharedPreferences sharedPreferences;
    private String userId;
    private String sessionToken;
    private String user;

    public LoginManager(
            TodoApi api,
            Converter<ResponseBody, ErrorResponse> converter,
            SharedPreferences sharedPreferences
    ) {
        this.api = api;
        this.converter = converter;
        this.sharedPreferences = sharedPreferences;

        userId = sharedPreferences.getString(USER_ID, null);
        sessionToken = sharedPreferences.getString(SESSION_TOKEN, null);
        user = sharedPreferences.getString(USER, null);
    }

    public boolean hasToLogin() {
        return sessionToken == null;
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getUser() {
        return user;
    }

    public void login(
            final String username,
            String password
    ) {

        Call<UserResponse> login = api.login(username, password);

        if (loginCallback != null) {
            loginCallback.setLoginEnabled(false);
        }

        login.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(
                    Call<UserResponse> call,
                    Response<UserResponse> response
            ) {
                if (loginCallback != null) {
                    loginCallback.setLoginEnabled(true);
                }
                if (response.isSuccessful()) {

                    sessionToken = response.body().getSessionToken();
                    userId = response.body().getObjectId();
                    user = response.body().getObjectId();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SESSION_TOKEN, sessionToken);
                    editor.putString(USER_ID, userId);
                    editor.putString(USER, user);
                    editor.apply();

                    if (loginCallback != null) {
                        loginCallback.loginOk();
                    }
                } else {
                    try {
                        ErrorResponse errorResponse = converter.convert(response.errorBody());
                        if (loginCallback != null) {
                            loginCallback.showErrorMessage(errorResponse.getError());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<UserResponse> call,
                    Throwable t
            ) {

                if (loginCallback != null) {
                    loginCallback.setLoginEnabled(true);
                    loginCallback.showErrorMessage(t.getMessage());
                }
            }
        });
    }
}
