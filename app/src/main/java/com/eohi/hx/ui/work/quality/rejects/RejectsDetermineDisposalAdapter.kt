package com.eohi.hx.ui.work.quality.rejects

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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

        v.tvZrgx.clicks {
            onGxClick?.let {
                it(position)
            }
        }
        v.tvZrr.clicks {
            onPersonClick?.let {
                it(position)
            }
        }


        if (v.etSl.tag is TextWatcher) {
            v.etSl.removeTextChangedListener(v.etSl.tag as TextWatcher)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (v.etSl.hasFocus()) {
                    s?.let {
                        if (TextUtils.isEmpty(s.toString())) {
                            onsetSL?.let {
                                it(position, "0")
                            }
                        } else {
                            onsetSL?.let {
                                it(position, s.toString())
                            }
                        }
                    }
                }
            }
        }
        v.etSl.addTextChangedListener(textWatcher)
        v.etSl.tag = textWatcher


        if (v.etSm.tag is TextWatcher) {
            v.etSm.removeTextChangedListener(v.etSm.tag as TextWatcher)
        }

        val mtextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (v.etSm.hasFocus()) {
                    s?.let {
                        onsetSM?.let {
                            it(position,s.toString())
                        }
                    }
                }
            }
        }
        v.etSm.addTextChangedListener(mtextWatcher)
        v.etSm.tag = textWatcher



    }

     var onClickCZFS: ((Int) -> Unit)? =null

     fun setOnCzfs(onClickCZFS: ((Int) -> Unit)?){
         this.onClickCZFS =onClickCZFS
     }

    var onClickBlxx: ((Int) -> Unit)? =null

    fun setClickBlxx(onClickBlxx: ((Int) -> Unit)?){
        this.onClickBlxx  = onClickBlxx
    }


    var onClickBlyy: ((Int) -> Unit)? =null
    fun setClickBlyy( onClickBlyy: ((Int) -> Unit)?){
        this.onClickBlyy = onClickBlyy
    }

     var onDelClick: ((Int) -> Unit)? =null
     fun onDel(onDelClick: ((Int) -> Unit)?){
        this.onDelClick = onDelClick
     }


    var onGxClick:((Int) -> Unit)?=null
    fun setGxClick( onGxClick:((Int) -> Unit)?){
        this.onGxClick = onGxClick
    }

    var onPersonClick:((Int) -> Unit)?=null
    fun setPersonClick(onPersonClick:((Int) -> Unit)?){
        this.onPersonClick = onPersonClick
    }

    var onsetSL:((Int,String) -> Unit)?=null
    fun setSl(onsetSL:((Int,String) -> Unit)?){
        this.onsetSL = onsetSL
    }

    var onsetSM:((Int,String) -> Unit)?=null
    fun setSm(onsetSM:((Int,String) -> Unit)?){
        this.onsetSM = onsetSM
    }

}