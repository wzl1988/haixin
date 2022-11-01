package com.eohi.hx.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.list_dialog.view.*

class ListDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var mMyListener: MyListener
    private var title: String? = null
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: DialogAdapter
    private var mContext: Context? = null
    private lateinit var view: View
    private lateinit var mGetAllListener: GetAllListener

    constructor(
        context: Context,
        t: String,
        listData: ArrayList<String>,
        listener: MyListener
    ) : this(
        context,
        R.style.ListsDialog
    ) {
        mContext = context
        title = t
        list = listData
        mMyListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DialogAdapter(mContext as Activity, list)
        adapter.itemClick {
            println("点击了item")
            dismiss()
            mMyListener.refreshActivity(it)
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.list_dialog, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        view.title.text = title
        view.iv_close clicks {
            dismiss()
        }
        view.btn_cancel clicks {
            dismiss()
        }
        view.btn_ok clicks {

        }
        view.btn_search.clicks {
            onSearchClick?.let {
                it(view.et_search.text.toString())
            }
        }
        view.btn_all clicks {
            mGetAllListener.getAll()
        }
        setContentView(view)
    }

    fun setGetAllListener(getAllListener: GetAllListener) {
        mGetAllListener = getAllListener
    }

    interface GetAllListener {
        fun getAll()
    }

    interface MyListener {
        fun refreshActivity(position: Int)
    }

    fun hideSearch() {
        view.et_search.gone()
        view.btn_search.gone()
    }

    fun showAllButton() {
        view.btn_all.show()
    }

    fun refreshList(newList: ArrayList<String>) {
        list = newList
        adapter.notifyDataSetChanged()
    }


    fun onSearchClick(c:((String)->Unit)?){
        this.onSearchClick =c
    }

    private var onSearchClick:((String)->Unit)? = null


}

