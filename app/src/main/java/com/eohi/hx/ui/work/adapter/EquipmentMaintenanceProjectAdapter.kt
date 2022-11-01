package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMaintainBinding
import com.eohi.hx.ui.work.equipment.model.MaintenannceProject

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/8/4 16:46
 */
class EquipmentMaintenanceProjectAdapter(
    mContext: Activity,
    listDatas: ArrayList<MaintenannceProject>
) : BaseAdapter<ItemMaintainBinding, MaintenannceProject>(mContext, listDatas) {
    override fun convert(v: ItemMaintainBinding, t: MaintenannceProject, position: Int) {
        v.tvTitle.text = t.BYFF + "  " + t.WBXMLB
        v.tvByff.text = t.WBXMMC
        v.tvBybz.text = t.BYBZ
        v.rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_wh -> {
                    onChecked?.let {
                        it(position, "完好")
                    }
                }
                R.id.rb_yc -> {
                    onChecked?.let {
                        it(position, "异常")
                    }
                }
                R.id.rb_dqr -> {
                    onChecked?.let {
                        it(position, "待确认")
                    }
                }
            }
        }

    }


    var onChecked: ((Int, String) -> Unit)? = null
    fun setOncheced(onChecked: ((Int, String) -> Unit)?) {
        this.onChecked = onChecked
    }


}