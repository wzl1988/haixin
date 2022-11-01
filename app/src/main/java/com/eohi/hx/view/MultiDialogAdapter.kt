package com.eohi.hx.view

import android.util.SparseBooleanArray
import androidx.appcompat.app.AppCompatActivity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.ItemMultiDialogBinding

class MultiDialogAdapter(
    context: AppCompatActivity,
    listDatas: ArrayList<String>,
    onmChecked: ((Int, Boolean) -> Unit)?
) :
    BaseAdapter<ItemMultiDialogBinding, String>(context, listDatas) {
    private var onChecked: ((Int, Boolean) -> Unit)? = onmChecked
    private val mCheckStates = SparseBooleanArray()

    override fun convert(v: ItemMultiDialogBinding, t: String, position: Int) {
        v.singleItem.text = t
        v.btnChoose.tag = position

        v.btnChoose.setOnCheckedChangeListener { buttonView, isChecked ->
            val pos = buttonView.tag as Int
            mCheckStates.put(pos, isChecked)

            onChecked?.let {
                it(position, isChecked)
            }
        }
        v.btnChoose.isChecked = mCheckStates.get(position, false)
    }


}