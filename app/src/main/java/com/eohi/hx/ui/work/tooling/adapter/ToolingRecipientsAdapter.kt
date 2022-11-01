package com.eohi.hx.ui.work.tooling.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemToolingRecipientsDetailBinding
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/12 17:11
 */
class ToolingRecipientsAdapter(mContext: Activity, listDatas: ArrayList<ToolinfoModel>)
    : BaseAdapter<ItemToolingRecipientsDetailBinding, ToolinfoModel>(mContext,listDatas) {
    override fun convert(v: ItemToolingRecipientsDetailBinding, t: ToolinfoModel, position: Int) {
        v.tvLyyy.text = t.lyyy
        v.tvCkh.text =t.ckh
        v.tvGg.text = t.gg
        v.tvGz.text = t.wpmc
        v.tvGzbh.text = t.gzbm
        val textWatcher =object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                setOnTextChange?.let {
                    if(!s.isNullOrEmpty())
                    it(s.toString(),position)
                }
            }

        }
        v.tvGzdd.removeTextChangedListener(textWatcher)
        v.tvGzdd.setText( t.dqszwz)
        v.tvGzdd.addTextChangedListener(textWatcher)
        v.tvKwh.text = t.kwh
        v.tvSqr.text = t.slr
    }

    var setOnTextChange:((String,Int)->Unit)? =null

    fun setOnTextChange(onTextChange:((String,Int)->Unit)){
        this.setOnTextChange =onTextChange
    }
}