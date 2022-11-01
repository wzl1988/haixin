package com.eohi.hx.ui.work.tooling.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemCutterBinding
import com.eohi.hx.widget.clicks

class CutterReplaceAdapter(mContext: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemCutterBinding, String>(mContext, listDatas) {
    private lateinit var changeListener: ChangeListener

    override fun convert(v: ItemCutterBinding, t: String, position: Int) {
        v.btnChange.clicks {
            changeListener.change()
        }
    }

    fun setChangeListener(changeListener: ChangeListener) {
        this.changeListener = changeListener
    }

    interface ChangeListener {
        fun change()
    }

}