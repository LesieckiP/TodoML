package com.soldiersofmobile.todoexpert;

import java.io.IOException;
import java.lang.annotation.Annotation;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soldiersofmobile.todoexpert.api.ErrorResponse;
import com.soldiersofmobile.todoexpert.api.TodoApi;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoApplication extends Application {

    private TodoApi todoApi;
    private Converter<ResponseBody, ErrorResponse> converter;
    private LoginManager loginManager;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(final Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("X-Parse-Application-Id", "X7HiVehVO7Zg9ufo0qCDXVPI3z0bFpUXtyq2ezYL")
                                .addHeader("X-Parse-REST-API-Key", "LCTpX53aBmbtIXOtFmDb9dklESKUd0q58hFbnRYc")
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        Gson gson = new GsonBuilder()
                //TODO setup gson
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        todoApi = retrofit.create(TodoApi.class);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginManager = new LoginManager(todoApi, converter, sharedPreferences);
    }

    public TodoApi getTodoApi() {
        return todoApi;
    }

    public Converter<ResponseBody, ErrorResponse> getConverter() {
        return converter;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
