package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemIncomgingHistoryListBinding
import com.eohi.hx.ui.work.model.IncomingListModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingDetailActivity
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.widget.clicks

class IncomingHistoryListAdapter(mContext: Activity, listData: ArrayList<IncomingListModel>) :
    BaseAdapter<ItemIncomgingHistoryListBinding, IncomingListModel>(mContext, listData) {

    override fun convert(v: ItemIncomgingHistoryListBinding, t: IncomingListModel, position: Int) {
        v.tvRwbh.text = t.RWDH
        v.tvCgddh.text = t.CGDDH
        v.tvTzdh.text = t.DHTZDH
        v.tvGys.text = t.GYSJC
        v.tvWpmc.text = t.WPMC
        v.tvCjsj.text = t.CJRQ
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
            val intent = Intent(mContext, IncomingDetailActivity::class.java).apply {
                putExtra("RWDH", t.RWDH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify clicks {
            val intent = Intent(mContext, IncomingCheckActivity::class.java).apply {
                putExtra("RWDH", t.RWDH)
                putExtra("type", "modify")
                putExtra("cgddh", t.CGDDH)
            }
            mContext.startActivity(intent)
        }
    }

}