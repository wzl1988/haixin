package com.eohi.hx.ui.work.quality.storage

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemFinishListBinding
import com.eohi.hx.ui.work.model.FinishCheckListResult
import com.eohi.hx.ui.work.quality.finish.FinishCheckActivity
import com.eohi.hx.ui.work.quality.finish.FinishDetailActivity
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.widget.clicks

class StorageListAdapter(mContext: Activity, listData: ArrayList<FinishCheckListResult>) :
    BaseAdapter<ItemFinishListBinding, FinishCheckListResult>(mContext, listData) {

    override fun convert(v: ItemFinishListBinding, t: FinishCheckListResult, position: Int) {
        v.tvJydh.text = t.JYDH
        v.tvGdh.text = t.GDH
        v.tvWpmc.text = t.WPMC
        v.tvGgms.text = t.GG
        v.tvJysj.text = t.JYSJ
        v.tvSapddh.text = t.SAPDDH
        if (!TextUtils.isEmpty(t.JYJG)) {
            if (t.JYJG == "1") {
                v.tvJyjg.text = "合格"
                v.tvCheckState.text = "合格"
                v.tvCheckState.setTextColor(R.color.qualified.asColor())
            } else {
                v.tvJyjg.text = "不合格"
                v.tvCheckState.text = "不合格"
                v.tvCheckState.setTextColor(R.color.unqualified.asColor())
            }
        }
        v.tvDetail clicks {
            val intent = Intent(mContext, StorageDetailActivity::class.java).apply {
                putExtra("GDH", t.GDH)
                putExtra("DJH", t.JYDH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify.visibility = View.GONE
        v.tvModify clicks {
            val intent = Intent(mContext, StorageActivity::class.java).apply {
                putExtra("DJH", t.JYDH)
                putExtra("type", "modify")
                putExtra("GDH", t.GDH)
            }
            mContext.startActivity(intent)
        }
    }

}