package com.eohi.hx.ui.work.my.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemProductionTeamEditBinding
import com.eohi.hx.ui.work.my.model.ProductionPersonModel
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/19 14:33
 */
class ProductionAddPersonAdapter (mContext: Activity, listDatas: ArrayList<ProductionPersonModel>) :
    BaseAdapter<ItemProductionTeamEditBinding, ProductionPersonModel>(mContext, listDatas) {
    override fun convert(v: ItemProductionTeamEditBinding, t: ProductionPersonModel, position: Int) {
        v.etXzmc.setText(t.XM)
        v.etZygh.setText(t.YGBH)
        if (v.etFpxs.tag is TextWatcher) {
            v.etFpxs.removeTextChangedListener(v.etFpxs.tag as TextWatcher)
        }
        v.etFpxs.setText(t.fpxs)
        val textWatcher= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                onEidtResult?.let {
                    it(position,s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
        v.tvDetele.clicks {
            onDeteleClick?.let {
                it(position)
            }
        }
        v.etFpxs.addTextChangedListener(textWatcher)
        v.etFpxs.tag = textWatcher
    }
    private var onDeteleClick: ((Int) -> Unit)? = null
    fun setDetele(onDeteleClick: ((Int) -> Unit)?){
        this.onDeteleClick = onDeteleClick
    }

    private var onEidtResult:((Int,String)-> Unit)?= null
    fun onEditListener(onEidtResult:((Int,String)-> Unit)?){
        this.onEidtResult = onEidtResult
    }


}