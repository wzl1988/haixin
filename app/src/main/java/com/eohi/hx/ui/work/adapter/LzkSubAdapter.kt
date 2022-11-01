package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemLzkSublistBinding
import com.eohi.hx.ui.work.model.LZKSubListModel

class LzkSubAdapter(mContext: Activity, listDatas: ArrayList<LZKSubListModel>) :
    BaseAdapter<ItemLzkSublistBinding, LZKSubListModel>(mContext, listDatas) {

    override fun convert(v: ItemLzkSublistBinding, t: LZKSubListModel, position: Int) {
        v.tvGx.text = t.gxms
        v.tvTls.text = t.scsl
        v.tvCfps.text = t.blsl
        v.tvHgs.text = t.dqgxljbgsl
    }

}