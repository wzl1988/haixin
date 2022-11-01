package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMaintainListBinding
import com.eohi.hx.ui.work.equipment.model.MaintainListModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/8/9 15:18
 */
class EquipmentMaintainListAdapter (mContext: Activity,
                                    listDatas: ArrayList<MaintainListModel>
) : BaseAdapter<ItemMaintainListBinding, MaintainListModel>(mContext, listDatas) {
    override fun convert(
        v: ItemMaintainListBinding,
        t: MaintainListModel,
        position: Int
    ) {
        v.tvSbbh.text  = t.SBBH
        v.tvSbmc.text = t.SBMC
        v.tvXh.text = t.XH
        v.tvWz.text = t.AZDD
        v.tvRwsj.text = t.CJSJ
    }
}