package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemToolingRecipientsListBinding
import com.eohi.hx.ui.work.tooling.model.ToolRecipientsListItem

class ToolingRecipientsListAdapter(mContext: Activity, listDatas: ArrayList<ToolRecipientsListItem>) :
    BaseAdapter<ItemToolingRecipientsListBinding, ToolRecipientsListItem>(mContext, listDatas) {

    override fun convert(v: ItemToolingRecipientsListBinding, t: ToolRecipientsListItem, position: Int) {
        v.textView5.text = t.gzmc
        v.textView7.text = t.gzbm
        v.textView9.text = t.lyrq
    }

}