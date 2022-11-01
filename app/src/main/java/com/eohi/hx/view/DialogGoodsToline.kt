package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.eohi.hx.R
import com.eohi.hx.ui.main.agvmodel.ProductionlineModel
import kotlinx.android.synthetic.main.dialog_goods_line.btn_cancle
import kotlinx.android.synthetic.main.dialog_goods_line.btn_ok
import kotlinx.android.synthetic.main.dialog_goods_line.tv_end

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/11 15:01
 */
class DialogGoodsToline(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var activity : Activity? =null
    var linelist: ArrayList<ProductionlineModel>?= null
    constructor(context: Context, a: Activity?,list: ArrayList<ProductionlineModel>?):this(context, R.style.MyDialog){
        activity = a
        linelist = list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_goods_line)
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
        tv_end.setOnClickListener {
            if(null!=linelist ){
                var poplist  = ArrayList<String>()
                for(i in linelist!!.indices){
                    poplist.add(linelist!![i].SCXMC)
                }
                showListPopulWindow(tv_end, poplist.toTypedArray(),linelist)
            }
        }

        btn_ok.setOnClickListener {
            itemClick?.let { it(tv_end.text.toString().trim())}
        }

    }

    fun onOkClick(itemClick: (String) -> Unit) {
        this.itemClick = itemClick
    }

    private var itemClick: ((String) -> Unit)? = null


    private fun showListPopulWindow(mEditText: TextView, list: Array<String>, linelist: ArrayList<ProductionlineModel>?) {
        val  listPopupWindow = ListPopupWindow(context)
        listPopupWindow.setAdapter(ArrayAdapter(context, R.layout.pop_item, list)) //用android内置布局，或设计自己的样式
        listPopupWindow.anchorView = mEditText //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.white)))
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //设置项点击监听
            mEditText.setText(linelist!![i].SCXBH) //把选择的选项内容展示在EditText上
            listPopupWindow.dismiss() //如果已经选择了，隐藏起来
        }
        listPopupWindow.show() //把ListPopWindow展示出来
    }



}