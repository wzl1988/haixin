package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.ui.work.adapter.DialogSelectAdapter
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_list.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/6 14:18
 */
class DialogSelectList(context: Context, themeResId: Int) : Dialog(context, themeResId)  {
    private  var activity : Activity? =null
    var title :String? = null
    var list:ArrayList<String>?=null
    var adpater: DialogSelectAdapter?=null

    constructor(context: Context, a: Activity?, t :String,l:ArrayList<String>?):this(context, R.style.MyDialog){
        activity = a
        title= t
        list = l
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_selectlist)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_title.text =title
        iv_close.clicks {
            dismiss()
        }
        initRc()
    }


    private fun initRc() {
        adpater= activity?.let { DialogSelectAdapter(it,list!!) }
        mRecyclerView.run {
            this.layoutManager = LinearLayoutManager(activity)
            this.adapter = adpater
        }
        adpater?.itemClick {
            dismiss()
            onItemClickListener?.run {
                this(it)
            }
        }

    }
    fun onItemClick(onItemClickListener:((Int)->Unit)?){
        this.onItemClickListener =onItemClickListener
    }
    private var onItemClickListener:((Int)->Unit)? = null


}