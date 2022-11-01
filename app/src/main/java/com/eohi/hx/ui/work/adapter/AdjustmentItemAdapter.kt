package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutAdjustmentItemBinding
import com.eohi.hx.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/21 10:38
 */
class AdjustmentItemAdapter (mContext: Activity, listDatas: ArrayList<ItemInfo>)
    : BaseAdapter<LayoutAdjustmentItemBinding, ItemInfo>(mContext,listDatas) {
    override fun convert(v: LayoutAdjustmentItemBinding, t: ItemInfo, position: Int) {
        v.tvTmh.text = t.tmh
        v.tvWpmc.text =t.WPMC
        v.tvGg.text = t.GGMS
        v.tvBeforeNum.text =t.ZSL
        v.tvAfterNum.text = t.tzhsl
    }

}