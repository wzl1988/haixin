package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.eohi.hx.R
import kotlinx.android.synthetic.main.dialog_auto_end.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 14:29
 */
class DialogAutoEnd(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var activity: Activity? = null
    var title: String? = null
    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"

    constructor(context: Context, a: Activity?, t: String) : this(context, R.style.MyDialog) {
        activity = a
        title = t
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_auto_end)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_title.text = title
        btn_cancle.setOnClickListener {
            dismiss()
        }

        btn_ok.setOnClickListener {
            if (TextUtils.isEmpty(et_start.text)) {
                Toast.makeText(context, "起点不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            itemClick?.let {
                it(et_start.text.toString().trim())
                et_start.setText("")
            }
        }
    }


    fun onOkClick(itemClick: (String) -> Unit) {
        this.itemClick = itemClick
    }

    private var itemClick: ((String) -> Unit)? = null

    private var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                var str = intent.getStringExtra("scannerdata").toString()
                str = str.replace("\u0000", "")
                val resultStr = str.replace("\\u0000", "")
                et_start.setText(resultStr)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity?.registerReceiver(scanReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
    }


}