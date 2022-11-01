package com.eohi.hx.view

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemDialogBinding

class DialogAdapter(context: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemDialogBinding, String>(context, listDatas) {

    override fun convert(v: ItemDialogBinding, t: String, position: Int) {
        v.singleItem.text = t
    }


}