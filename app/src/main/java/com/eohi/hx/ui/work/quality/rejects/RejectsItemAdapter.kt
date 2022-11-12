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
        v.tvWph.text = t.WPH+"("+t.wpmc+"+"+t.gg+")"
        v.tvWpmc.text = t.cjmc
        v.tvScgx.text = t.GXSM
        v.tvCx.text = t.jgdymc
        v.tvBls.text = t.BZS.toString()
        v.tvDjr.text = t.djr
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
            intent.putExtra("bls",t.BZS.toString())
            intent.putExtra("scry",t.CJYH)
            intent.putExtra("zzrxm",t.ZRRXM)
            intent.putExtra("zzrbm",t.ZRRGH)
            intent.putExtra("jgdy",t.jgdybh)
            intent.putExtra("rwdh",t.SCRWDH)
            intent.putExtra("swrq",t.SWRQ)
            intent.putExtra("djr",t.djr)
            intent.putExtra("djrid",t.djrid)
            intent.putExtra("jgdybh",t.jgdybh)
            intent.putExtra("jgdymc",t.jgdymc)
            intent.putExtra("sbbh",t.SBBH)
            intent.putExtra("sbmc",t.SBMC)
            intent.putParcelableArrayListExtra("detail",t.detail)
            intent.setClass(mContext,RejectsDetermineActivity::class.java)
            mContext.startActivity(intent)
        }
    }
}