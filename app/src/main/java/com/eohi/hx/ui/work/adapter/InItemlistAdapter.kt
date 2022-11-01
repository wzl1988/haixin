package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutInItemBinding
import com.eohi.hx.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/6 10:50
 */
class InItemlistAdapter(mContext: Activity, listDatas: ArrayList<ItemInfo>)
    :BaseAdapter<LayoutInItemBinding,ItemInfo>(mContext,listDatas) {
    override fun convert(v: LayoutInItemBinding, t: ItemInfo, position: Int) {
        v.tvTmh.text = t.tmh
        v.tvWpmc.text =t.khwph
        v.tvDw.text =t.cxh
        v.tvGg.text = t.scrwdh
        v.tvNum.text =t.ZSL
    }

}