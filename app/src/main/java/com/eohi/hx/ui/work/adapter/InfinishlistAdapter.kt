package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutFinishItemBinding
import com.eohi.hx.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/6 10:50
 */
class InfinishlistAdapter(mContext: Activity, listDatas: ArrayList<ItemInfo>)
    :BaseAdapter<LayoutFinishItemBinding,ItemInfo>(mContext,listDatas) {
    override fun convert(v: LayoutFinishItemBinding, t: ItemInfo, position: Int) {
        v.tvTmh.text = t.tmh
        v.tvWpmc.text =t.WPMC
        v.tvDw.text =t.ZDW
        v.tvGg.text = t.GGMS
        v.tvNum.text =t.ZSL
        v.tvSap.text  =t.ERPSCDDH
        v.tvKhwl.text = t.khwph
    }

}