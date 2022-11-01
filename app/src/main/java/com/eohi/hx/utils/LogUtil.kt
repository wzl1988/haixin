package com.eohi.hx.utils

import android.util.Log
import androidx.viewbinding.BuildConfig

object LogUtil {
    private const val TAG = "haixin_log"
    private const val TAG_NET = "haixin_net"

    fun i(message: String?) {
        if (BuildConfig.DEBUG) Log.i(TAG, message)
    }

    fun e(message: String?) {
        if (BuildConfig.DEBUG) Log.e(TAG, message)
    }

    fun showHttpHeaderLog(message: String?) {
        if (BuildConfig.DEBUG) Log.d(TAG_NET, message)
    }

    fun showHttpApiLog(message: String?) {
        if (BuildConfig.DEBUG) Log.w(TAG_NET, message)
    }

    fun showHttpLog(message: String?) {
        if (BuildConfig.DEBUG) Log.i(TAG_NET, message)
    }
}