package com.eohi.hx.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast


object ToastUtil {
    fun showToast(context: Context?, message: String?,i:Int= Toast.LENGTH_SHORT ) {
        if (!TextUtils.isEmpty(message)) Toast.makeText(context, message, i)
            .show()
    }
}