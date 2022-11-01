package com.eohi.hx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.widget.clicks

class MultiListDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var mMyListener: MyListener
    private var title: String? = null
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: MultiDialogAdapter
    private var mContext: Context? = null
    private lateinit var listPosition: ArrayList<Int>
    private lateinit var view: View

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
        listPosition = ArrayList()
        adapter = MultiDialogAdapter(mContext as AppCompatActivity, list, ::onChecked)
//        adapter.itemClick {
//            println("点击了item")
//            dismiss()
//            mMyListener.refreshActivity(it)
//        }
        view = LayoutInflater.from(mContext).inflate(R.layout.list_dialog, null)

        view.findViewById<LinearLayout>(R.id.btn_container).visibility = View.VISIBLE

        val recyclerView = view.findViewById<RecyclerView>(R.id.mRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<ImageView>(R.id.iv_close).clicks {
            dismiss()
        }
        view.findViewById<Button>(R.id.btn_cancel).clicks {
            dismiss()
        }
        view.findViewById<Button>(R.id.btn_ok).clicks {
            dismiss()
            mMyListener.refreshActivity(listPosition)
        }
        setContentView(view)
    }

    fun hideSearch() {
        view.findViewById<EditText>(R.id.et_search).gone()
        view.findViewById<Button>(R.id.btn_search).gone()
    }

    private fun onChecked(i: Int, b: Boolean) {
        if (b) {
            listPosition.add(i)
        } else {
            listPosition.remove(i)
        }
    }

    interface MyListener {
        fun refreshActivity(listPosition: ArrayList<Int>)
    }

}

