package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.eohi.hx.R
import com.eohi.hx.ui.main.agvmodel.ProductionlineModel
import com.eohi.hx.utils.ToastUtil
import kotlinx.android.synthetic.main.dialog_to_line.btn_cancle
import kotlinx.android.synthetic.main.dialog_to_line.btn_ok
import kotlinx.android.synthetic.main.dialog_to_line.et_start
import kotlinx.android.synthetic.main.dialog_to_line.tv_end
import kotlinx.android.synthetic.main.dialog_to_line.tv_title

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/11 15:34
 */
class DialogtoLine (context: Context, themeResId: Int) : Dialog(context, themeResId){

    var activity : Activity? =null
    var linelist: ArrayList<ProductionlineModel>?= null
    var title :String? = null
    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    constructor(context: Context, a: Activity?, t:String, list: ArrayList<ProductionlineModel>?):this(context, R.style.MyDialog){
        activity = a
        linelist = list
        title = t
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_to_line)
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
            if(et_start.text.isEmpty()){
                ToastUtil.showToast(activity,"??????????????????")
                return@setOnClickListener
            }
            if(tv_end.text.isEmpty()){
                ToastUtil.showToast(activity,"??????????????????")
                return@setOnClickListener
            }
            if(null != itemClick){
                itemClick.let {
                    if (it != null) {
                        it(et_start.text.toString().trim(),tv_end.text.toString())
                    }
                }
            }

        }

    }


    private fun showListPopulWindow(mEditText: EditText, list: Array<String>, linelist: ArrayList<ProductionlineModel>?) {
        val  listPopupWindow = ListPopupWindow(context)
        listPopupWindow.setAdapter(ArrayAdapter(context, R.layout.pop_item, list)) //???android???????????????????????????????????????
        listPopupWindow.anchorView = mEditText //???????????????????????????????????????mEditText?????????
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.white)))
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //?????????????????????
            mEditText.setText(linelist!![i].SCXBH) //?????????????????????????????????EditText???
            listPopupWindow.dismiss() //????????????????????????????????????
        }
        listPopupWindow.show() //???ListPopWindow????????????
    }


    fun onOkClick(itemClick: (String,String) -> Unit) {
        this.itemClick = itemClick
    }

    private var itemClick: ((String,String) -> Unit)? = null

    private var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                var str = intent.getStringExtra("scannerdata").toString()
                str = str.replace("\u0000", "")
                val resultStr= str.replace("\\u0000", "")
                et_start.setText(resultStr)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // ?????????????????????
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity?.registerReceiver(scanReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        //??????????????????
        activity?.unregisterReceiver(scanReceiver)
    }



}