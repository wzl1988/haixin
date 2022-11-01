package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.text.Html
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemEquipmentMaintenanceListBinding
import com.eohi.hx.ui.work.equipment.model.TaskPartModel

class EquipmentMaintenanceListAdapter(
    mContext: Activity,
    listDatas: ArrayList<TaskPartModel>
) : BaseAdapter<ItemEquipmentMaintenanceListBinding, TaskPartModel>(mContext, listDatas) {
    override fun convert(v: ItemEquipmentMaintenanceListBinding, t: TaskPartModel, position: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>部件编号：</font><font color='#666666'>"
                        + t.WBBJBH + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>部件名称：</font><font color='#666666'>"
                        + t.WBBJMC + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.wbsj.text = Html.fromHtml(
                "<font color='#333333'>预定维保时间：</font><font color='#666666'>"
                        + t.BCJHWBRQ + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>部件编号：</font><font color='#666666'>"
                        + t.WBBJBH + "</font>"
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>部件名称：</font><font color='#666666'>"
                        + t.WBBJMC + "</font>"
            )
            v.wbsj.text = Html.fromHtml(
                "<font color='#333333'>预定维保时间：</font><font color='#666666'>"
                        + t.BCJHWBRQ + "</font>"
            )
        }
    }
}