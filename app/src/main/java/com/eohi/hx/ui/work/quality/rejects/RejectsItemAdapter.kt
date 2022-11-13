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
        v.tvBlpdj.text = t.swh
        v.tvLzk.text = t.sclzkkh
        v.tvWph.text = t.wph+"("+t.wpmc+"+"+t.gg+")"
        v.tvWpmc.text = t.cjmc
        v.tvScgx.text = t.gxsm
        v.tvCx.text = t.jgdymc
        v.tvBls.text = t.bzs.toString()
        v.tvDjr.text = t.djr
        v.tvDjsj.text = t.cjrq
        v.tvJgdy.text = t.jgdymc
        v.btnGo.clicks {
            val intent = Intent()
            intent.putExtra("swh",t.swh)
            intent.putExtra("lzkkh", t.sclzkkh)
            intent.putExtra("wph",t.wph)
            intent.putExtra("wpmc",t.wpmc)
            intent.putExtra("th","")
            intent.putExtra("gx",t.gxsm)
            intent.putExtra("gxh",t.gxh)
            intent.putExtra("bls",t.bzs.toString())
            intent.putExtra("scry",t.CJYH)
            intent.putExtra("zzrxm",t.zrrxm)
            intent.putExtra("zzrbm",t.zrrgh)
            intent.putExtra("jgdy",t.jgdybh)
            intent.putExtra("rwdh",t.scrwdh)
            intent.putExtra("swrq",t.swrq)
            intent.putExtra("djr",t.djr)
            intent.putExtra("djrid",t.djrid)
            intent.putExtra("jgdybh",t.jgdybh)
            intent.putExtra("jgdymc",t.jgdymc)
            intent.putExtra("sbbh",t.sbbh)
            intent.putExtra("sbmc",t.sbmc)
            intent.putParcelableArrayListExtra("detail",t.detail)
            intent.setClass(mContext,RejectsDetermineActivity::class.java)
            mContext.startActivity(intent)
        }
    }
}