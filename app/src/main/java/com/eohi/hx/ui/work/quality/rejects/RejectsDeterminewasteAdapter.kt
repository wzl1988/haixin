package com.eohi.hx.ui.work.quality.rejects

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemWasteBinding
import com.eohi.hx.ui.work.quality.rejects.model.Clbf
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/11 10:15
 */
class RejectsDeterminewasteAdapter(mContext: Activity,
                                   listDatas: ArrayList<Clbf>
) :BaseAdapter<ItemWasteBinding, Clbf>(mContext, listDatas,){
    override fun convert(v: ItemWasteBinding, t: Clbf, position: Int) {
        v.tvWp.text = t.clwph
        v.tvFs.text = t.clczfs
        v.tvBlxx.text = t.clblxx
        v.etSl.setText(t.clbfsl.toString())

        v.tvWp.clicks {
            onSetWp?.let {
                it(position)
            }
        }
        v.tvBlxx.clicks {
            onClickBlxx?.let {
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

        v.tvDel.clicks {
            onDelClick?.let {
                it(position)
            }
        }

    }

    var onSetWp:((Int)->Unit)?=null
    fun setWP( onSetWp:((Int)->Unit)?){
        this.onSetWp = onSetWp
    }
    var onClickBlxx: ((Int) -> Unit)? =null

    fun setClickBlxx(onClickBlxx: ((Int) -> Unit)?){
        this.onClickBlxx  = onClickBlxx
    }

    var onsetSL:((Int,String) -> Unit)?=null
    fun setSl(onsetSL:((Int,String) -> Unit)?){
        this.onsetSL = onsetSL
    }

    var onDelClick: ((Int) -> Unit)? =null
    fun onDel(onDelClick: ((Int) -> Unit)?){
        this.onDelClick = onDelClick
    }


}