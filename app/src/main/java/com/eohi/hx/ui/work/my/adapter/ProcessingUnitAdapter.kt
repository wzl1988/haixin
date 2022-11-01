package com.eohi.hx.ui.work.my.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemProcessUnitBinding
import com.eohi.hx.ui.work.my.model.MyProcessingUnitModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/18 14:10
 */
class ProcessingUnitAdapter(mContext: Activity, listDatas: ArrayList<MyProcessingUnitModel>) :
    BaseAdapter<ItemProcessUnitBinding, MyProcessingUnitModel>(mContext, listDatas) {
    override fun convert(v: ItemProcessUnitBinding, t: MyProcessingUnitModel, position: Int) {
        v.tvBh.text = t.SCXBH
        v.tvMc.text = t.SCXMC
    }
}