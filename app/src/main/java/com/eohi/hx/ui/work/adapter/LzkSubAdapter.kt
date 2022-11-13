package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemLzkSublistBinding
import com.eohi.hx.ui.work.model.LzkItem

class LzkSubAdapter(mContext: Activity, listDatas: ArrayList<LzkItem>) :
    BaseAdapter<ItemLzkSublistBinding, LzkItem>(mContext, listDatas) {

    override fun convert(v: ItemLzkSublistBinding, t: LzkItem, position: Int) {
        v.tvGx.text = t.gxms
        v.tvCzg.text = t.scrxm
        v.tvZxbgsj.text = t.bgsj
        v.tvBgs.text =t.dqgxljbgsl.toString()
        v.tvBlps.text =t.blsl.toString()
        v.tvHgs.text = t.hgsl.toString()
        v.tvBfs.text =t.bfsl.toString()
        v.tvFxs.text = t.fxsl.toString()
        v.tvCcsl.text =t.ccsl.toString()
    }

}