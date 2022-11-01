package com.eohi.hx.ui.work.tooling.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemToolOfflineBinding
import com.eohi.hx.ui.work.tooling.model.ToolListModel
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/10/9 10:41
 */
class ToolOfflineAdapter(mContext: Activity, listDatas: ArrayList<ToolListModel>) :BaseAdapter<ItemToolOfflineBinding, ToolListModel> (
    mContext,listDatas
){
    override fun convert(v: ItemToolOfflineBinding, t: ToolListModel, position: Int) {
        v.tvGzbh.text = t.GZBH
        v.tvGzmc.text = t.GZMC
        v.tvGgxh.text = t.GZGG
        v.tvScr.text = t.SXR
        v.tvSj.text = t.SXSJ
        v.tvCz.clicks {
            offLineClick?.let {
                it(position)
            }
        }
    }

    var offLineClick:((Int)->Unit)? =null
    fun setOfflineClick(offLineClick:((Int)->Unit)?){
        this.offLineClick = offLineClick
    }
}