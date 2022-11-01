package com.eohi.hx.ui.work.adapter

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemEquipmentCheck2Binding
import com.eohi.hx.ui.work.equipment.model.EquipmentParts
import com.eohi.hx.view.SpacesItemDecoration

class EquipmentCheck2Adapter(
    mContext: Activity,
    listDatas: ArrayList<EquipmentParts>,
    onmChecked: ((Int, Int, String) -> Unit)?
) :
    BaseAdapter<ItemEquipmentCheck2Binding, EquipmentParts>(mContext, listDatas) {
    private var onChecked: ((Int, Int, String) -> Unit)? = onmChecked
    override fun convert(v: ItemEquipmentCheck2Binding, t: EquipmentParts, position: Int) {
        v.number.text = "部件编号：" + t.BJBH
        v.bjmc.text = "部件名称：" + t.BJMC

        fun onCheck(i: Int, s: String) {
            onChecked?.let {
                it(position, i, s)
            }
        }

        val adapter = EquipmentCheckAdapter(mContext, t.item, ::onCheck)
        v.rc.layoutManager = LinearLayoutManager(mContext)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 1, 0))

    }

}