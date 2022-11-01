package com.eohi.hx.ui.work.mould

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.FragmentMouldInDetailBinding
import com.eohi.hx.ui.work.adapter.MouldListAdapter
import com.eohi.hx.view.UILoader
import com.scwang.smartrefresh.layout.SmartRefreshLayout

class MouldInDetailFragment :
    BaseFragment<BaseViewModel, FragmentMouldInDetailBinding>(), UILoader.OnRetryClickListener {

    private lateinit var adapter: MouldListAdapter
    private lateinit var lists: ArrayList<String>
    private lateinit var mUiLoader: UILoader
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.errorData.observe(this, {
            mUiLoader.updateStatus(UILoader.UIStatus.ERROR)
        })
    }

    override fun initView() {
        adapter = MouldListAdapter(mContext, lists)

        mUiLoader = object : UILoader(mContext) {
            override fun getSuccessView(container: ViewGroup): View {
                return createSuccessView(container)
            }
        }

        v.container.removeAllViews()
        v.container.addView(mUiLoader)
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS)
        mUiLoader.setonRetryClickListener(this)
    }

    private fun createSuccessView(container: ViewGroup): View {
        val recycle =
            LayoutInflater.from(mContext).inflate(R.layout.common_recycle, container, false)
        mRecyclerView = recycle.findViewById(R.id.mRecyclerView)
        mSmartRefreshLayout = recycle.findViewById(R.id.refreshLayout)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.adapter = adapter
        return recycle
    }

    override fun initClick() {
    }

    override fun initData() {
        lists = ArrayList()
        for (i in 1..100) {
            lists.add("1")
        }
    }

    override fun lazyLoadData() {
    }

    override fun onRetryClick() {

    }

}