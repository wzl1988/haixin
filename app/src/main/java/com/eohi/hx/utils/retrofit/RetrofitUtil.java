package com.eohi.hx.utils.retrofit;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    // log拦截器  打印所有的log
    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger(){
        @Override
        public void log(String message) {
            Log.i("HttpLogging",message);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(new MyHttpLoggingInterceptor()).
            connectTimeout(40, TimeUnit.SECONDS).
            readTimeout(40, TimeUnit.SECONDS).
            writeTimeout(40, TimeUnit.SECONDS).build();
    public static Retrofit.Builder builder = new Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create());
}
