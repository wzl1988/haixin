package com.eohi.hx.utils

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.eohi.hx.R
import com.eohi.hx.ui.login.LoginActivity

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/8 10:54
 */
class KillSelfService :Service(){

    /**
     *
     * 关闭应用后多久重新启动
     *
     */
    private var stopDelayed: Long = 1000

    private var handler: Handler? = null

    override fun onCreate() {
        super.onCreate()
        handler = Handler()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler!!.postDelayed({
            if (android.os.Build.VERSION.SDK_INT < 29) {
                var i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            } else {
                startAc()
            }
            this@KillSelfService.stopSelf()
        }, stopDelayed)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun startAc() {
        val notificationUtils = NotificationUtils(this@KillSelfService)
        val content = "点击重启应用"
        notificationUtils.sendNotificationFullScreen(getString(R.string.app_name), content, "type")
    }

}