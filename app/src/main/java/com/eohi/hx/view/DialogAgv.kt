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
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.eohi.hx.R
import kotlinx.android.synthetic.main.dialog_agv.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 13:52
 */
class DialogAgv(context: Context, themeResId :Int) : Dialog(context, themeResId) {
    private  var activity :Activity? =null
    var title :String? = null
   constructor(context: Context,a: Activity?,t :String):this(context,R.style.MyDialog){
       activity = a
       title= t
   }

    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_agv)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_title.text =title
        btn_cancle.setOnClickListener {
            dismiss()
        }

        btn_ok.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(et_end.text)) {
                Toast.makeText(context, "终点不能为空！", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            itemClick?.let { it(et_end.text.toString().trim())
                et_end.setText("")}
        })

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
                val resultStr= str.replace("\\u0000", "")
                et_end.setText(resultStr.trim())
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