package com.eohi.hx.ui.work.tooling.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemToolOnlineBinding
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/10/9 14:54
 */
class ToolOnLineAdapter(mContext: Activity,listdata:ArrayList<ToolinfoModel>) : BaseAdapter<ItemToolOnlineBinding, ToolinfoModel>(
    mContext,listdata
) {
    override fun convert(v: ItemToolOnlineBinding, t: ToolinfoModel, position: Int) {
        v.tvGzbh.text = t.gzbm
        v.tvGzmc.text = t.wpmc
        v.tvGgxh.text = t.gg
    }
}