package com.eohi.hx.ui.work.quality.unqualified.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMxBinding
import com.eohi.hx.ui.work.model.Items
import com.eohi.hx.widget.clicks

/**
 *@author: YH
 *@date: 2022/1/17
 *@desc: 明细适配器
 */
class MxAdapter(mContext: Activity, list: ArrayList<Items>) :
    BaseAdapter<ItemMxBinding, Items>(mContext, list) {

    private lateinit var deleteListener: DeleteListener
    private lateinit var blyyListener: BlyyListener
    private lateinit var slListener: SlListener
    private lateinit var smlis:SmListener
    @SuppressLint("ClickableViewAccessibility")
    override fun convert(v: ItemMxBinding, t: Items, position: Int) {
        v.tvBhgyy.text = t.blxx
        v.etSl.setText(t.blsl.toString())
        v.etSm.setText(t.sm)
        v.tvBhgyy clicks {
            blyyListener.setBlyy(position)
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
                            slListener.setSl(position, "0")
                        } else {
                            slListener.setSl(position, s.toString())
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
                        smlis.setSm(position,s.toString())
                    }
                }
            }
        }
        v.etSm.addTextChangedListener(mtextWatcher)
        v.etSm.tag = textWatcher



        v.tvCz clicks {
            deleteListener.delete(position)
        }
    }

    fun setDeleteListener(deleteListener: DeleteListener) {
        this.deleteListener = deleteListener
    }

    fun setBlyyListener(blyyListener: BlyyListener) {
        this.blyyListener = blyyListener
    }


    fun setSlListener(slListener: SlListener) {
        this.slListener = slListener
    }

    fun setSmLis(sizelin:SmListener){
        this.smlis = sizelin
    }

    interface DeleteListener {
        fun delete(position: Int)
    }

    interface BlyyListener {
        fun setBlyy(position: Int)
    }


    interface SlListener {
        fun setSl(position: Int, s: String)
    }


    interface SmListener {
        fun setSm(position: Int,s: String)
    }

}

