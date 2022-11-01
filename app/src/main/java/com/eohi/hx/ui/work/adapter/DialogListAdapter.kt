package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.PopItemBinding
import com.eohi.hx.ui.work.model.SupplierModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/25 11:09
 */
class DialogListAdapter (mContext: Activity, listDatas: ArrayList<SupplierModel>)
    : BaseAdapter<PopItemBinding, SupplierModel>(mContext,listDatas) {
    override fun convert(v: PopItemBinding, t: SupplierModel, position: Int) {
        v.btn.text = t.gysmc+"("+t.gysh+")"
    }
}