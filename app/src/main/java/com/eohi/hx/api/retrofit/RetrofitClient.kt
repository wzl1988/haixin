package com.eohi.hx.api.retrofit

import android.util.Log
import com.eohi.hx.api.ApiService
import com.eohi.hx.utils.Preference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    private var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    companion object {
        fun getInstance() =
            SingletonHolder.INSTANCE

        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }



    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(apiurl)
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {

        // log拦截器  打印所有的log
        val interceptor = HttpLoggingInterceptor { message ->
            Log.i(
                "HttpLogging",
                message
            )
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .sslSocketFactory(SSLContextSecurity.createIgnoreVerifySSL("TLS"))
            .build()
    }

    fun create(): ApiService = retrofit.create(
        ApiService::class.java
    )


}