package com.eohi.hx.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Environment
import android.util.TypedValue
import com.eohi.hx.App
import com.eohi.hx.R

import java.io.File


private val displayMetrics = Resources.getSystem().displayMetrics

//扩展属性
val Float.px get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

//转px
val Int.dp get() = this.toFloat().px


fun Float.dp2Px(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}


fun Float.px2Dp(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * 获取状态栏高度
 * @return
 */
fun getStatusBarHeight(context: Context): Int {
    var result = 0
    //获取状态栏高度的资源id
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}


object SysUtils {

    // 获取当前APP名称
    fun getAppName(context: Context): String? {
        val packageManager = context.packageManager
        val applicationInfo: ApplicationInfo
        applicationInfo = try {
            packageManager.getApplicationInfo(context.packageName, 0)
        } catch (e: java.lang.Exception) {
            return context.resources.getString(R.string.app_name)
        }
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    fun getAppVersion(): String? {
        val context: Context = App.instance
        val manager: PackageManager = context.packageManager
        return try {
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "1.0.0"
        }
    }

    fun getAppVersionCode(): Int {
        val context: Context = App.instance
        val manager: PackageManager = context.packageManager
        return try {
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            1
        }
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getSystemModel(): String? {
        return try {
            Build.MODEL
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    fun getDeviceBrand(): String? {
        return try {
            Build.BRAND
        } catch (e: Exception) {
            ""
        }
    }

    fun initFiles() {
        var file = File(Environment.getExternalStorageDirectory(), "MVVM/data")
        if (!file.exists()) file.mkdirs()
        file = File(Environment.getExternalStorageDirectory(), "MVVM/images")
        if (!file.exists()) file.mkdirs()
        file = File(Environment.getExternalStorageDirectory(), "MVVM/download")
        if (!file.exists()) file.mkdirs()
    }

    fun getScreenWidth(activity: Activity): Int {
        var width = 0
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        width = display.width
        return width
    }

    fun getScreenHeight(activity: Activity): Int {
        var height = 0
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        height = display.height
        return height
    }
}