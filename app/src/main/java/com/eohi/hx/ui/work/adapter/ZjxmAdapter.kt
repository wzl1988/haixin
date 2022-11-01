package com.eohi.hx.ui.work.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemZjxmBinding
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.quality.delivery.DeliveryDetailActivity
import com.eohi.hx.ui.work.quality.finish.FinishDetailActivity
import com.eohi.hx.ui.work.quality.first.FirstDetailActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingDetailActivity
import com.eohi.hx.ui.work.quality.process.ProcessDetailActivity

/**
 * 通用子列表适配器
 */
class ZjxmAdapter(
    context: AppCompatActivity,
    listDatas: ArrayList<BtBean>,
    onmChecked: ((Int, Boolean) -> Unit)?,
    onResult: ((Int, String) -> Unit)?
) :
    BaseAdapter<ItemZjxmBinding, BtBean>(context, listDatas) {
    private var onChecked: ((Int, Boolean) -> Unit)? = onmChecked
    private var onTextResult: ((Int, String) -> Unit)? = onResult

    override fun convert(v: ItemZjxmBinding, t: BtBean, position: Int) {
        v.tvXmbm.text = t.XMBM
        v.tvXmmc.text = t.XMMC
        v.tvJybz.text = t.JYBZ
        v.tvBzz.text = t.BZZ
        v.tvJygj.text = t.JYGJ
        v.etJyjl.setText(t.JYZ)
        v.switchCheckResult.isChecked = t.PDJG == "1"

        if (mContext.javaClass == IncomingDetailActivity::class.java || mContext.javaClass == FirstDetailActivity::class.java
            || mContext.javaClass == ProcessDetailActivity::class.java || mContext.javaClass == FinishDetailActivity::class.java
            || mContext.javaClass == DeliveryDetailActivity::class.java
        ) {
            v.etJyjl.isEnabled = false
            v.switchCheckResult.isEnabled = false
        } else {
            v.etJyjl.addTextChangedListener {
                val text = it.toString()
                onTextResult?.let {
                    it(position, text)
                }
            }
            v.switchCheckResult.setOnCheckedChangeListener { _, isChecked ->
                onChecked?.let {
                    it(position, isChecked)
                }
            }
        }

    }

}


