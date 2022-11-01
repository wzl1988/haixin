package com.eohi.hx.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.eohi.hx.R
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.layout_alarmlog_pop.view.*
import kotlinx.android.synthetic.main.layout_alarmlog_pop.view.tv_start
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/2/21 11:16
 */
class PopAlarmLogSearch(context: Context) : PopupWindow(context) {
    lateinit var mylistener:MyListener
    var userid by Preference("userid", "")
    private var startDate: Date? = null
    private var endDate: Date? = null
    lateinit var context: Context
    var str =""
    constructor(context: Context,listener:MyListener):this(context){
        this.mylistener = listener
        var view = LayoutInflater.from(context).inflate(R.layout.layout_alarmlog_pop, null)
        this.contentView = view
        this.context = context
        isFocusable = true
        isTouchable = true
        bindView()
    }


    private fun bindView() {
        contentView.tv_start.clicks {
            startDate = DateUtil.chooseStartDate(context, contentView.tv_start, startDate, endDate)
        }
        contentView.tv_end.clicks {
            endDate = DateUtil.chooseStartDate(context, contentView.tv_end, startDate, endDate)
        }
        contentView.tv_userid.setText(userid)
        contentView.btn_reset.clicks {
            contentView.tv_start.text = ""
            contentView.tv_end.text =""
            contentView.et_info.setText("")
        }
        val list = ArrayList<String>()
        list.add("是")
        list.add("否")
        list.add("全部")
        val ad= MySpinnerAdapter(context, android.R.layout.simple_spinner_item, list)
        contentView.spinner.adapter =ad

        contentView.spinner.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0->{
                    str = "1"
                }
                1->{
                    str = "0"
                }
                2->{
                    str = ""
                }
            }

        }

        contentView.btn_search.clicks {
            mylistener.onSearckClick(
                contentView.tv_start.text.toString(),
                contentView.tv_end.text.toString(),
                contentView.et_info.text.toString(),
                str,
                contentView.tv_userid.text.toString()
            )
            dismiss()
        }
    }


    fun setCode(code:String){
        contentView.et_info.setText(code)
    }

    interface MyListener {
        fun onSearckClick(start:String,end:String,code:String,isread:String,userid:String)
    }


}