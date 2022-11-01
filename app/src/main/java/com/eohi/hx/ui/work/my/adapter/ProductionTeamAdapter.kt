package com.eohi.hx.ui.work.my.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemProductionTeamBinding
import com.eohi.hx.ui.work.my.model.MyProductionTeamModel
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/18 17:05
 */
class ProductionTeamAdapter(mContext: Activity, listDatas: ArrayList<MyProductionTeamModel>) :BaseAdapter<ItemProductionTeamBinding, MyProductionTeamModel>(mContext, listDatas) {
    override fun convert(v: ItemProductionTeamBinding, t: MyProductionTeamModel, position: Int) {
        v.tvXzdm.text = t.XZBH
        v.tvXzmc.text = t.xzmc
        v.tvSj.text = t.CJRQ
        v.tvUpdate.clicks {
            onUpdateClick?.let {
               it(position)
            }
        }
        v.tvDetele.clicks {
            onDeteleClick?.let {
                it(position)
            }
        }
    }

    fun setUpdate(onUpdateClick: ((Int) -> Unit)?){
        this.onUpdateClick = onUpdateClick
    }

    fun setDetele(onDeteleClick: ((Int) -> Unit)?){
        this.onDeteleClick = onDeteleClick
    }
    private var onUpdateClick: ((Int) -> Unit)? = null
    private var onDeteleClick: ((Int) -> Unit)? = null
}