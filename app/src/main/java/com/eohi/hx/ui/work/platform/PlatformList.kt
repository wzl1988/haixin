package com.eohi.hx.ui.work.platform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityPlatformListBinding
import com.eohi.hx.ui.work.adapter.PlatformListAdapter
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.UILoader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.common_toolbar.view.*

class PlatformList : BaseActivity<BaseViewModel, ActivityPlatformListBinding>(),
    UILoader.OnRetryClickListener {

    private lateinit var adapter: PlatformListAdapter
    private lateinit var lists: ArrayList<String>
    private lateinit var mUiLoader: UILoader
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "月台置为空闲状态记录"
        v.root.toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter = PlatformListAdapter(this, lists)

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
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter
        return recycle
    }

    override fun initClick() {
    }

    override fun initData() {
        lists = ArrayList()
        lists.add("1")
        lists.add("2")
    }

    override fun initVM() {
        vm.errorData.observe(this,{
            mUiLoader.updateStatus(UILoader.UIStatus.ERROR)
        })
    }

    override fun onRetryClick() {
        //重新请求数据
    }

}