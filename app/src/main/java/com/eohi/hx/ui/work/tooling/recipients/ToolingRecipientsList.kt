package com.eohi.hx.ui.work.tooling.recipients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.api.HttpUtil
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityToolingRecipientsListBinding
import com.eohi.hx.ui.work.adapter.ToolingRecipientsListAdapter
import com.eohi.hx.ui.work.tooling.model.ToolRecipientsListItem
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.UILoader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.common_toolbar.view.*

class ToolingRecipientsList : BaseActivity<BaseViewModel, ActivityToolingRecipientsListBinding>(),
    UILoader.OnRetryClickListener {
    var listdata = MutableLiveData<ArrayList<ToolRecipientsListItem>>()
    private lateinit var adapter: ToolingRecipientsListAdapter
    private lateinit var lists: ArrayList<ToolRecipientsListItem>
    private lateinit var mUiLoader: UILoader
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "工装领用列表"
        v.root.toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter = ToolingRecipientsListAdapter(this, lists)

        mUiLoader = object : UILoader(this) {
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
        val recycle = LayoutInflater.from(this).inflate(R.layout.common_recycle, container, false)
        mRecyclerView = recycle.findViewById(R.id.mRecyclerView)
        mSmartRefreshLayout = recycle.findViewById(R.id.refreshLayout)
        mSmartRefreshLayout.setOnRefreshListener {
            vm.launchList({HttpUtil.getInstance().getService().getLyList(companyNo, accout)},listdata,isShowLoading = true,successCode = 200)
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter
        return recycle
    }

    override fun initClick() {
    }

    override fun initData() {
        lists = ArrayList()
        vm.launchList({HttpUtil.getInstance().getService().getLyList(companyNo, accout)},listdata,isShowLoading = true,successCode = 200)
    }

    override fun initVM() {
        vm.errorData.observe(this) {
            mUiLoader.updateStatus(UILoader.UIStatus.ERROR)
        }
        listdata.observe(this, Observer {
            mSmartRefreshLayout.finishRefresh()
            lists.clear()
            lists.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onRetryClick() {
        //重新请求数据
    }

}