package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemTransferInventoryBinding
import com.eohi.hx.ui.work.model.TransferKwWpModel

/**
 *@author: YH
 *@date: 2021/12/7
 *@desc: 调库存栈板适配器
 */
class TransferInventoryAdapter(mContext: Activity, list: ArrayList<TransferKwWpModel>) :
    BaseAdapter<ItemTransferInventoryBinding, TransferKwWpModel>(mContext, list) {

    override fun convert(v: ItemTransferInventoryBinding, t: TransferKwWpModel, position: Int) {
        v.tvWph.text = t.wph
        v.tvSl.text = t.sl
    }

}