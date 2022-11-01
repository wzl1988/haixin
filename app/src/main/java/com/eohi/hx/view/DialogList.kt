package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.ui.work.adapter.DialogListAdapter
import com.eohi.hx.ui.work.model.SupplierModel
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_list.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/25 10:35
 */
class DialogList(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    private var activity: Activity? = null
    var title: String? = null
    var list: ArrayList<SupplierModel>? = null
    var adpater: DialogListAdapter? = null

    constructor(context: Context, a: Activity?, t: String, l: ArrayList<SupplierModel>?) : this(
        context,
        R.style.MyDialog
    ) {
        activity = a
        title = t
        list = l

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_list)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        tv_title.text = title
        iv_close.clicks {
            dismiss()
        }
        btn_search.clicks {
            onSearchClick?.let {
                if (et_search.text.isNotEmpty())
                    it(et_search.text.toString())
            }
        }
        initRc()
    }

    private fun initRc() {
        adpater = activity?.let { DialogListAdapter(it, list!!) }
        mRecyclerView.run {
            this.layoutManager = LinearLayoutManager(activity)
            this.adapter = adpater
        }
        adpater?.itemClick {
            dismiss()
            onItemClickListener?.run {
                this(list!![it])
            }
        }

    }

    fun clearData() {
        et_search.setText("")
    }

    fun updateList(l: ArrayList<SupplierModel>?) {
        list?.clear()
        list?.addAll(l!!)
        adpater?.notifyDataSetChanged()
    }

    fun onSearch(onSearchClick: ((String) -> Unit)?) {
        this.onSearchClick = onSearchClick
    }

    fun onItemClick(onItemClickListener: ((SupplierModel) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    private var onSearchClick: ((String) -> Unit)? = null

    private var onItemClickListener: ((SupplierModel) -> Unit)? = null


}