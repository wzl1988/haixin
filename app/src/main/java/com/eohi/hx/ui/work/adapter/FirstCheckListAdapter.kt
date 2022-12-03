package com.eohi.hx.ui.work.adapter

import android.app.Activity
import android.content.Intent
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemFirstCheckListBinding
import com.eohi.hx.ui.work.model.FirstCheckListResult
import com.eohi.hx.ui.work.quality.first.FirstCheckActivity
import com.eohi.hx.widget.clicks

class FirstCheckListAdapter(mContext: Activity, listData: ArrayList<FirstCheckListResult>) :
    BaseAdapter<ItemFirstCheckListBinding, FirstCheckListResult>(mContext, listData) {

    override fun convert(v: ItemFirstCheckListBinding, t: FirstCheckListResult, position: Int) {
        v.tvSjdh.text = t.zjrwdh
        v.tvLzkh.text = t.lzkkh
        v.tvRwbh.text = t.rwdh
        v.tvScddh.text = t.sapddh
        v.tvWph.text = t.wph
        v.tvWpmc.text = t.wpmc
        v.tvGgms.text = t.gg
        v.tvGx.text = t.gxmc+"("+t.gxh+")"
        v.tvSl.text = t.jysl.toString()
        v.tvCjsj.text = t.jysj
        v.tvJyy.text = t.jyr
        v.tvCj.text = t.cjmc
        v.tvJgdy.text = t.jgdymc
        v.tvJyms.text = t.jyms
        v.btnCheck clicks {
            val intent = Intent(mContext, FirstCheckActivity::class.java).apply {
                putExtra("sjdh", t.zjrwdh)
                putExtra("lzkh", t.lzkkh)
                putExtra("rwbh", t.rwdh)
                putExtra("sapddh", t.sapddh)
                putExtra("wph", t.wph)
                putExtra("wpmc",t.wpmc)
                putExtra("gg",t.gg)
                putExtra("cjh",t.cjh)
                putExtra("cjmc",t.cjmc)
                putExtra("jgdybh",t.jgdybh)
                putExtra("jgdymc",t.jgdymc)
                putExtra("gxh",t.gxh)
                putExtra("gxmc",t.gxmc)
                putExtra("sjsl",t.jysl)
                putExtra("jysj",t.jysj)
            }
            mContext.startActivity(intent)
        }

    }

}