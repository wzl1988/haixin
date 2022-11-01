package com.eohi.hx.api

import com.eohi.hx.api.retrofit.RetrofitClient

class HttpUtil {

    //LazyThreadSafetyMode.SYNCHRONIZED
    private val mService by lazy { RetrofitClient.getInstance().create() }

    //suspend fun test(options: LinkedHashMap<String, String?>) = mService.test(options)

    //suspend fun getArticleList(page: Int) = mService.getArticleList(page)


    companion object {
        @Volatile
        private var httpUtil: HttpUtil? = null

        fun getInstance() = httpUtil ?: synchronized(this) {
            httpUtil ?: HttpUtil().also { httpUtil = it }
        }
    }

    //可以直接在BaseViewModel中获取取ApiService对象，简化接口调用
    fun getService(): ApiService {
        return mService
    }

}
