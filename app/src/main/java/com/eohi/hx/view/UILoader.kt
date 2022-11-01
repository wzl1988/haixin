package com.eohi.hx.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.eohi.hx.App
import com.eohi.hx.R

abstract class UILoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class UIStatus {
        LOADING, SUCCESS, ERROR, EMPTY, NONE
    }

    private lateinit var mLoadingView: View
    private lateinit var mSuccessView: View
    private lateinit var mErrorView: View
    private lateinit var mEmptyView: View
    private var mCurrentStatus = UIStatus.NONE
    private var mListener: OnRetryClickListener? = null

    init {
        switchUIByCurrentStatus()
    }

    fun updateStatus(uiStatus: UIStatus) {
        mCurrentStatus = uiStatus
        App.sHandler?.post {
            switchUIByCurrentStatus()
        }
    }

    private fun switchUIByCurrentStatus() {
        mLoadingView = getLoadingView()
        addView(mLoadingView)
        mLoadingView.visibility = if (mCurrentStatus == UIStatus.LOADING) VISIBLE else GONE
        mEmptyView = getEmptyView()
        addView(mEmptyView)
        mEmptyView.visibility = if (mCurrentStatus == UIStatus.EMPTY) VISIBLE else GONE
        mErrorView = getErrorView()
        addView(mErrorView)
        mErrorView.visibility = if (mCurrentStatus == UIStatus.ERROR) VISIBLE else GONE
        mSuccessView = getSuccessView(this)
        addView(mSuccessView)
        mSuccessView.visibility = if (mCurrentStatus == UIStatus.SUCCESS) VISIBLE else GONE
    }

    private fun getEmptyView() =
        LayoutInflater.from(context).inflate(R.layout.fragment_empty_view, this, false)

    private fun getLoadingView() =
        LayoutInflater.from(context).inflate(R.layout.fragment_loading_view, this, false)

    private fun getErrorView(): View {
        val networkErrorView: View =
            LayoutInflater.from(context).inflate(R.layout.fragment_error_view, this, false)
        networkErrorView.findViewById<View>(R.id.error_icon).setOnClickListener {
            mListener?.onRetryClick()
        }
        return networkErrorView
    }

    protected abstract fun getSuccessView(container: ViewGroup): View

    fun setonRetryClickListener(listener: OnRetryClickListener?) {
        mListener = listener
    }

    interface OnRetryClickListener {
        fun onRetryClick()
    }

}