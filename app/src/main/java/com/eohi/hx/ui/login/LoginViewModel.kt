package com.eohi.hx.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.ApiService
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.api.retrofit.SSLContextSecurity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.login.model.CompanyModel
import com.eohi.hx.ui.login.model.LoginModel
import com.eohi.hx.ui.main.model.LoginResultModel
import com.eohi.hx.utils.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/9 14:37
 */

class LoginViewModel : BaseViewModel() {
    var loginresut = MutableLiveData<LoginResultModel>()
    var companylist = MutableLiveData<ArrayList<CompanyModel>>()
    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    fun getLoginResult(loginmodel: LoginModel, isShowLoading: Boolean) {
        showLoading()
        viewModelScope.launch {
            try {
                loginresut.value=withContext(Dispatchers.IO){
                    Retrofit.Builder()
                        .client(getOkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(apiurl)
                        .build().create(
                            ApiService::class.java).login(loginmodel) }
                dismissLoading()
            }catch (e:Exception){
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            }finally {
                dismissLoading()
            }
        }
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


    fun getCompany(){
        launchList({  Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(apiurl)
            .build().create(
                ApiService::class.java).getCompany()},companylist,true)
    }


}