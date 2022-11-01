package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.PopItemBinding

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/6 14:54
 */
class DialogSelectAdapter(mContext: Activity, listDatas: ArrayList<String>)
    : BaseAdapter<PopItemBinding, String>(mContext,listDatas)  {
    override fun convert(v: PopItemBinding, t: String, position: Int) {
        v.btn.text =t
    }
}