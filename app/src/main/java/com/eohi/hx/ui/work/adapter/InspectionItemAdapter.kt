package com.eohi.hx.ui.work.adapter

import android.app.Activity
import androidx.core.widget.addTextChangedListener
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemZjxmBinding
import com.eohi.hx.ui.work.model.InspectionitemModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/22 16:10
 */
class InspectionItemAdapter(mContext: Activity, listDatas: ArrayList<InspectionitemModel>
, onResult: ((Int, String) -> Unit)?) :BaseAdapter<ItemZjxmBinding, InspectionitemModel> (
    mContext, listDatas
){
    private var onTextResult: ((Int, String) -> Unit)? = onResult

    override fun convert(v: ItemZjxmBinding, t: InspectionitemModel, position: Int) {
        v.tvXmmc.text =t.xmmc
        v.tvXmbm.text = t.xmbm
        v.tvJybz.text = "上限："+t.zdz +" 下限："+t.zxz
        v.tvJygj.text  = t.pdyj
        v.tvBzz.text = t.bzz
        v.etJyjl.setText(t.scz)
        if(t.pdjg.isNullOrEmpty()){
            t.pdjg = "1"
        }else{
            if(t.pdjg=="正常" || t.pdjg=="1"){
                v.yes.isChecked = true
            }else{
                v.no.isChecked = true
            }
        }

        v.rg.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.yes->{
                    t.pdjg = "1"
                }
                R.id.no->{
                    t.pdjg = "2"
                }
            }
        }

        v.etJyjl.addTextChangedListener {
            val text = it.toString()
            onTextResult?.let {
                it(position, text)
            }
        }
    }
}