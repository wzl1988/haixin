package com.eohi.hx.ui.work.quality.rejects

import android.app.Activity
import android.content.Intent
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutRejectsItemBinding
import com.eohi.hx.ui.work.quality.rejects.model.RejectsListModel
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/8 10:29
 */
class RejectsItemAdapter(mContext: Activity, listDatas: ArrayList<RejectsListModel>) :BaseAdapter<LayoutRejectsItemBinding, RejectsListModel>(
    mContext, listDatas,
) {
    override fun convert(v: LayoutRejectsItemBinding, t: RejectsListModel, position: Int) {
        v.tvBlpdj.text = t.SWH
        v.tvLzk.text = t.SCLZKKH
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.wpmc
        v.tvScgx.text = t.GXSM
        v.tvBls.text = t.bhgzsl.toString()
        v.tvDjr.text = t.CJYH
        v.tvDjsj.text = t.CJRQ
        v.tvJgdy.text = t.jgdymc
        v.btnGo.clicks {
            val intent = Intent()
            intent.putExtra("swh",t.SWH)
            intent.putExtra("lzkkh", t.SCLZKKH)
            intent.putExtra("wph",t.WPH)
            intent.putExtra("wpmc",t.wpmc)
            intent.putExtra("th","")
            intent.putExtra("gx",t.GXSM)
            intent.putExtra("gxh",t.gxh)
            intent.putExtra("bls",t.bhgzsl.toString())
            intent.putExtra("scry",t.CJYH)
            intent.putExtra("zzrxm",t.ZRRXM)
            intent.putExtra("jgdy",t.jgdybh)
            intent.setClass(mContext,RejectsDetermineActivity::class.java)
            mContext.startActivity(intent)
        }
    }
}