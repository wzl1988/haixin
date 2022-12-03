package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemFirstHistoryListBinding
import com.eohi.hx.ui.work.model.FirstCheckListResult
import com.eohi.hx.ui.work.model.OldFirstCheckListResult
import com.eohi.hx.ui.work.quality.first.FirstCheckActivity
import com.eohi.hx.ui.work.quality.first.FirstDetailActivity
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.hide
import com.eohi.hx.widget.clicks

class FirstHistoryListAdapter(mContext: Activity, listData: ArrayList<OldFirstCheckListResult>) :
    BaseAdapter<ItemFirstHistoryListBinding, OldFirstCheckListResult>(mContext, listData) {

    override fun convert(v: ItemFirstHistoryListBinding, t: OldFirstCheckListResult, position: Int) {
        v.tvRwbh.text = t.RWBH
        v.tvGdh.text = t.SCGDH
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.WPMC
        v.tvCjsj.text = t.CJSJ
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
            val intent = Intent(mContext, FirstDetailActivity::class.java).apply {
                putExtra("gdh", t.SCGDH)
                putExtra("rwbh", t.RWBH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify.hide()
        v.tvModify clicks {
            val intent = Intent(mContext, FirstCheckActivity::class.java).apply {
                putExtra("gdh", t.SCGDH)
                putExtra("rwbh", t.RWBH)
                putExtra("type", "modify")
            }
            mContext.startActivity(intent)
        }
    }

}