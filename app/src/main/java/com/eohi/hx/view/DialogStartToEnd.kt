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
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_start_end.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 14:44
 */
class DialogStartToEnd(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var activity : Activity? = null
    var title :String? = null
    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    constructor(context: Context, a: Activity?, t :String):this(context, R.style.MyDialog){
        activity = a
        title= t
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_start_end)
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

        btn_ok.clicks {
            if (TextUtils.isEmpty(et_end.text)) {
                Toast.makeText(context, "终点不能为空！", Toast.LENGTH_SHORT).show()
                return@clicks
            }
            if (TextUtils.isEmpty(et_start.text)) {
                Toast.makeText(context, "起点不能为空！", Toast.LENGTH_SHORT).show()
                return@clicks
            }

            itemClick?.let {
                it(et_start.text.toString().trim(),et_end.text.toString().trim())
                et_start.setText("")
                et_end.setText("")
            }
        }
    }




    fun onOkClick(itemClick: (String,String) -> Unit) {
        this.itemClick = itemClick
    }

    private var itemClick: ((String,String) -> Unit)? = null

    private var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                var str = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                str = str.replace("\u0000", "")
                val resultStr= str.replace("\\u0000", "")
                if (et_start.isFocused) {
                    et_start.setText(resultStr)
                    et_end.isFocusable = true
                    et_end.requestFocus()
                }else if (et_end.isFocused) {
                    et_end.setText(resultStr)
                }

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