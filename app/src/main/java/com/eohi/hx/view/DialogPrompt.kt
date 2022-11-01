package com.eohi.hx.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.eohi.hx.R
import com.eohi.hx.widget.clicks

class DialogPrompt(context: Context?, themeResId: Int) : AlertDialog(context, themeResId) {

    constructor(context: Context?) : this(context, R.style.MyDialog)

    private lateinit var ivClose: ImageView
    private lateinit var tvCancel: TextView
    private lateinit var tvSure: TextView
    private lateinit var onClickListener: OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dailog)
        setCanceledOnTouchOutside(false)
        initView()
        initClick()
    }

    private fun initClick() {
        ivClose clicks {
            dismiss()
        }
        tvCancel clicks {
            dismiss()
        }
        tvSure clicks {
            onClickListener.onPositiveClick()
        }
    }

    private fun initView() {
        ivClose = findViewById(R.id.iv_close)
        tvCancel = findViewById(R.id.tv_cancel)
        tvSure = findViewById(R.id.tv_sure)
    }

    interface OnClickListener {
        fun onPositiveClick()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}