package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.widget.clicks
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.dialog_equ.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/29 13:57
 */
class DialogEqu(context: Context?, themeResId: Int) : Dialog(context!!, themeResId) {
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: DialogAdapter
    private var mContext: Context? = null
    private lateinit var mMyListener: MyListener
    constructor(
        context: Context,
        listData: ArrayList<String>,
        listener: MyListener
    ) : this(
        context,
        R.style.MyDialog
    ) {
        mContext = context
        list = listData
        mMyListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_equ)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp

        adapter = DialogAdapter(mContext as Activity, list)
        adapter.itemClick {
            dismiss()
            mMyListener.refreshActivity(it)
        }
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.adapter = adapter
        iv_close clicks {
            dismiss()
        }
        tablayout.addTab(tablayout.newTab().setText("生产设备"))
        tablayout.addTab(tablayout.newTab().setText("全部设备"))
        tablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text == "生产设备") {
                    onEquClick?.let {
                        it()
                    }
                }else{
                    onAllEquClick?.let {
                        it()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }


    private var onEquClick: (() -> Unit)? = null
    private var onAllEquClick:(() -> Unit)? = null


    fun setOnEqu(onEquClick: (() -> Unit)?) {
        this.onEquClick = onEquClick
    }

    fun setOnAllequ( onAllEquClick:(() -> Unit)?){
        this.onAllEquClick =onAllEquClick
    }

    interface MyListener {
        fun refreshActivity(position: Int)
    }
    fun refreshList(newList: ArrayList<String>) {
        list = newList
        adapter.notifyDataSetChanged()
    }

}