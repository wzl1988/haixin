package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemProcessListBinding
import com.eohi.hx.ui.work.model.ProcessCheckListResult
import com.eohi.hx.ui.work.quality.process.ProcessCheckActivity
import com.eohi.hx.ui.work.quality.process.ProcessDetailActivity
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.widget.clicks

class ProcessListAdapter(mContext: Activity, listData: ArrayList<ProcessCheckListResult>) :
    BaseAdapter<ItemProcessListBinding, ProcessCheckListResult>(mContext, listData) {

    override fun convert(v: ItemProcessListBinding, t: ProcessCheckListResult, position: Int) {
        v.tvJydh.text = t.JYDH
        v.tvGdh.text = t.GDH
        v.tvCx.text = t.CX
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.WPMC
        v.tvJygx.text = t.JYGX
        v.tvJysj.text = t.JYSJ
        v.tvSapddh.text = t.SAPDDH
        if (!TextUtils.isEmpty(t.JYJG)) {
            if (t.JYJG == "1") {
                v.tvCheckState.text = "合格"
                v.tvCheckState.setTextColor(R.color.qualified.asColor())
            } else {
                v.tvCheckState.text = "不合格"
                v.tvCheckState.setTextColor(R.color.unqualified.asColor())
            }
        }
        v.tvDetail clicks {
            val intent = Intent(mContext, ProcessDetailActivity::class.java).apply {
                putExtra("GDH", t.GDH)
                putExtra("GXH", t.GXH)
                putExtra("DJH", t.JYDH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify clicks {
            if (t.GXH == null) {
                Toast.makeText(mContext, "没有录入工序！", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(mContext, ProcessCheckActivity::class.java).apply {
                    putExtra("DJH", t.JYDH)
                    putExtra("type", "modify")
                    putExtra("GDH", t.GDH)
                    putExtra("GXH", t.GXH)
                    putExtra("GX", t.JYGX)
                }
                mContext.startActivity(intent)
            }
        }
    }

}