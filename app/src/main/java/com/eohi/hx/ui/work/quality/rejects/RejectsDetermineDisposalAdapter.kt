package com.eohi.hx.ui.work.quality.rejects

import android.app.Activity
import android.widget.TextView
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemCzfsBinding
import com.eohi.hx.ui.work.quality.rejects.model.SubmitRejectsItem
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/10 13:23
 */
class RejectsDetermineDisposalAdapter(mContext: Activity, listDatas: ArrayList<SubmitRejectsItem>) :BaseAdapter<ItemCzfsBinding, SubmitRejectsItem>(
    mContext, listDatas,
){
    override fun convert(v: ItemCzfsBinding, t: SubmitRejectsItem, position: Int) {
        v.tvCzfs.text = t.czfs
        v.tvBlxx.text = t.blxx
        v.tvBlyy.text = t.blyy
        v.etSl.setText(t.blsl.toString())
        v.tvZrgx.text = t.zrgxm
        v.tvZrr.text = t.scr
        v.etSm.setText(t.sm)

        v.tvCzfs.clicks {
            onClickCZFS?.let {
                it(position)
            }
        }
        v.tvBlxx.clicks {
            onClickBlxx?.let {
                it(position)
            }
        }
        v.tvDel.clicks {
            onDelClick?.let {
                it(position)
            }
        }
        v.tvBlyy.clicks {
            onClickBlyy?.let {
                it(position)
            }
        }

    }

     var onClickCZFS: ((Int) -> Unit?)? =null

     fun setOnCzfs(onClickCZFS: ((Int) -> Unit?)?){
         this.onClickCZFS =onClickCZFS
     }

    var onClickBlxx: ((Int) -> Unit?)? =null

    fun setClickBlxx(onClickBlxx: ((Int) -> Unit?)?){
        this.onClickBlxx  = onClickBlxx
    }


    var onClickBlyy: ((Int) -> Unit?)? =null
    fun setClickBlyy( onClickBlyy: ((Int) -> Unit?)?){
        this.onClickBlyy = onClickBlyy
    }

     var onDelClick: ((Int) -> Unit?)? =null
     fun onDel(onDelClick: ((Int) -> Unit?)?){
        this.onDelClick = onDelClick
     }



}