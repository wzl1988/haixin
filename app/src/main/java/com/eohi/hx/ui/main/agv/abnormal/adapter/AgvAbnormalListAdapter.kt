package com.eohi.hx.ui.main.agv.abnormal.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemAgvabnormalListBinding
import com.eohi.hx.ui.main.agv.abnormal.DealAgvAbnormalActivity
import com.eohi.hx.ui.main.agv.abnormal.RepostAgvAbnormalActivity
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalListBean
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.widget.clicks

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc:agv异常处理列表适配器
 */
class AgvAbnormalListAdapter(mContext: Activity, list: ArrayList<AgvAbnormalListBean>) :
    BaseAdapter<ItemAgvabnormalListBinding, AgvAbnormalListBean>(mContext, list) {

    private lateinit var refreshListener: RefreshListener

    override fun convert(v: ItemAgvabnormalListBinding, t: AgvAbnormalListBean, position: Int) {
        v.tvRwid.text = t.rwid
        v.tvYwlx.text = t.ywlx
        v.tvRwzt.text = t.rwzt
        v.tvCh.text = t.ch
        v.tvQd.text = t.qd
        v.tvZd.text = t.zd
        v.tvCjr.text = t.cjrid
        v.tvCjsj.text = t.cjsj
        v.tvZhfksj.text = t.ZHZXSJ

        if (t.rwzt != "已取消" && t.rwzt != "执行失败" && t.rwzt != "发送失败") {
            v.btnDeal.gone()
        }

        v.btnCancel clicks {
            mContext.showAlertDialog("确定", "重要提示", "是否取消") {
                refreshListener.refresh(t.rwid)
            }
        }

        v.btnDeal clicks {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable("AgvAbnormalListBean", t)
            intent.putExtras(bundle)
            if (t.rwzt == "已取消" || t.rwzt == "执行失败") {
                intent.setClass(mContext, DealAgvAbnormalActivity::class.java)
            }
            if (t.rwzt == "发送失败") {
                intent.setClass(mContext, RepostAgvAbnormalActivity::class.java)
            }
            mContext.startActivity(intent)
        }
    }

    fun setRefreshListener(refreshListener: RefreshListener) {
        this.refreshListener = refreshListener
    }

    interface RefreshListener {
        fun refresh(rwid: String)
    }

}