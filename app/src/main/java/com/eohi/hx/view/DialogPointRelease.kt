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
import kotlinx.android.synthetic.main.dialog_release.*
import java.util.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/3 14:12
 */
class DialogPointRelease(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var activity : Activity? = null
    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    var set = TreeSet<String>()
    constructor(context: Context, a: Activity?):this(context, R.style.MyDialog){
        activity = a
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_release)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        btn_cancle.setOnClickListener {
            dismiss()
        }

        btn_ok.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(et_start.text)) {
                Toast.makeText(context, "站点不能为空！", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            itemClick?.let {
                var strbuff = StringBuffer()
                set.forEach {
                    strbuff.append(it)
                    strbuff.append(",")
                }
                if(set.isEmpty()){
                    it(et_start.text.toString())
                }else{
                    it(strbuff.toString())}
                }


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
                et_start.setText(resultStr.trim())
                set.add(resultStr.trim())
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