package com.eohi.hx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.eohi.hx.R
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_agv_setting.*
/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/1/18 15:02
 */
class DialogAgvSetting (context: Context, themeResId :Int) : Dialog(context, themeResId){
    var tag = 0
    constructor(i:Int,context: Context):this(context, R.style.MyDialog){
        this.tag =i
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_agv_setting)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_cancel.clicks { dismiss() }
        iv_close.clicks { dismiss() }
        if(tag ==0){
            unpass.isChecked = true
        }else{
            pass.isChecked = true
        }
        rg.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.pass->
                    tag =1
                R.id.unpass->
                    tag =0
            }

        }
        tv_ok.clicks {
            onOkClick?.let {
                dismiss()
                it(tag)
            }
        }
    }

    fun setOnOkClick(onOkClick: ((Int) -> Unit)? ){
        this.onOkClick= onOkClick
    }

    private var onOkClick: ((Int) -> Unit)? = null
}