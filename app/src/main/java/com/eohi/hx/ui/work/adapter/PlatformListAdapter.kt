package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemPlatformListBinding

class PlatformListAdapter(mContext: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemPlatformListBinding, String>(mContext, listDatas) {

    override fun convert(v: ItemPlatformListBinding, t: String, position: Int) {

    }

}