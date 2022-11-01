package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.text.Html
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemEquipmentFaultConfirmListBinding
import com.eohi.hx.ui.work.equipment.model.Fault

class EquipmentFaultConfirmListAdapter(
    mContext: Activity,
    listDatas: ArrayList<Fault>
) : BaseAdapter<ItemEquipmentFaultConfirmListBinding, Fault>(mContext, listDatas) {
    override fun convert(v: ItemEquipmentFaultConfirmListBinding, t: Fault, position: Int) {
        // TODO: 2021/7/18  
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>设备编号：</font><font color='#666666'>"
                        + t.SBBH + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>设备名称：</font><font color='#666666'>"
                        + t.SBMC + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.sbxh.text = Html.fromHtml(
                "<font color='#333333'>设备型号：</font><font color='#666666'>"
                        + t.XH + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.szdz.text = Html.fromHtml(
                "<font color='#333333'>安装地点：</font><font color='#666666'>"
                        + t.SBDQSZWZ + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.jjcd.text = Html.fromHtml(
                "<font color='#333333'>紧急程度：</font><font color='#666666'>"
                        + t.JJCD + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.bxsj.text = Html.fromHtml(
                "<font color='#333333'>报修时间：</font><font color='#666666'>"
                        + t.FSGZSJ + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.wbsj.text = Html.fromHtml(
                "<font color='#333333'>报修人：</font><font color='#666666'>"
                        + t.ZDQRYHM + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>设备编号：</font><font color='#666666'>"
                        + t.SBBH + "</font>"
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>设备名称：</font><font color='#666666'>"
                        + t.SBMC + "</font>"
            )
            v.sbxh.text = Html.fromHtml(
                "<font color='#333333'>设备型号：</font><font color='#666666'>"
                        + t.XH + "</font>"
            )
            v.szdz.text = Html.fromHtml(
                "<font color='#333333'>安装地点：</font><font color='#666666'>"
                        + t.GSMC + "</font>"
            )
            v.jjcd.text = Html.fromHtml(
                "<font color='#333333'>紧急程度：</font><font color='#666666'>"
                        + t.JJCD + "</font>"
            )
            v.bxsj.text = Html.fromHtml(
                "<font color='#333333'>报修时间：</font><font color='#666666'>"
                        + t.FSGZSJ + "</font>"
            )
            v.wbsj.text = Html.fromHtml(
                "<font color='#333333'>报修人：</font><font color='#666666'>"
                        + t.ZDQRYHM + "</font>"
            )
        }
    }
}