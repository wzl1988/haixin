package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.graphics.Color
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMaintainHistoryBinding
import com.eohi.hx.ui.work.equipment.model.EquMaintainHistory
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/8/10 16:04
 */
class EquipmentMaintainHistoryAdapter (mContext: Activity,
                                       listDatas: ArrayList<EquMaintainHistory>
) : BaseAdapter<ItemMaintainHistoryBinding, EquMaintainHistory>(mContext, listDatas){
    override fun convert(v: ItemMaintainHistoryBinding, t: EquMaintainHistory, position: Int) {
        v.tvStatus.text = t.qczt
        if(t.qczt =="签出"){
            v.tvTime.text = t.qcsj
            v.ivIcon.setImageResource(R.mipmap.equ_main_stop)
            v.tvStatus.setTextColor(Color.parseColor("#F1A809"))
            v.ivIcon.clicks {
                onFinish?.let {
                    it(position)
                }
            }
        }else if(t.qczt =="完成"){
            v.tvTime.text = t.qcsj+"\n"+ t.qrsj
            v.ivIcon.setImageResource(R.mipmap.equ_main_start)
            v.tvStatus.setTextColor(Color.parseColor("#16C68E"))
        }

        v.tvYh.text =t.wxr
        v.btnDetele.clicks {
            onDelete?.let {
                it(position)
            }
        }


    }

    var onDelete:((Int)->Unit)? =null
    fun setOndeleteClick(d:((Int)->Unit)?){
        this.onDelete = d
    }

    var onFinish:((Int)->Unit)?=null
    fun setOnFinishclick(f:((Int)->Unit)?){
        this.onFinish = f
    }

}