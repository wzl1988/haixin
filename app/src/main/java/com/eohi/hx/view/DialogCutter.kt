package com.eohi.hx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.eohi.hx.R
import com.eohi.hx.widget.clicks

class DialogCutter(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var mMyListener: MyListener
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: MultiDialogAdapter
    private var mContext: Context? = null
    private lateinit var tv_ghsj: TextView

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
        setContentView(R.layout.dialog_cutter)
        findViewById<ImageView>(R.id.iv_close) clicks {
            dismiss()
        }
        findViewById<Button>(R.id.btn_ok) clicks {

        }
        tv_ghsj = findViewById(R.id.tv_ghsj)
        tv_ghsj clicks {

        }
    }

    interface MyListener {
        fun refreshActivity(listPosition: ArrayList<Int>)
    }

}