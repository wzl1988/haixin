package com.eohi.hx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.eohi.hx.R
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_alert.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/19 13:37
 */
class DialogAlert(context: Context, themeResId :Int) : Dialog(context, themeResId) {
    var content:String=""
    constructor(context: Context,content:String):this(context, R.style.MyDialog){
        this.content=content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_content.text = content
        tv_cancel.clicks { dismiss() }
        iv_close.clicks { dismiss() }
        tv_ok.clicks {
            onOkClick?.let {
                dismiss()
                it()
            }
        }
    }

    fun setOnOkClick(onOkClick: (() -> Unit)? ){
        this.onOkClick= onOkClick
    }

    private var onOkClick: (() -> Unit)? = null


}