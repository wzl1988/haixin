package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMouldListBinding

class MouldListAdapter(mContext: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemMouldListBinding, String>(mContext, listDatas) {

    override fun convert(v: ItemMouldListBinding, t: String, position: Int) {

    }

}