package com.eohi.hx.ui.work.quality.incoming

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityIncomingListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.IncomingListAdapter
import com.eohi.hx.ui.work.model.IncomingListModel
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import java.util.*

class IncomingListActivity : BaseActivity<IncomingViewModel, ActivityIncomingListBinding>() {

    private lateinit var hashMap: HashMap<String, String>
    private lateinit var popView: View
    private lateinit var etGys: EditText
    private lateinit var etWpmc: EditText
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private var page: Int = 1
    private lateinit var adapter: IncomingListAdapter
    private lateinit var list: ArrayList<IncomingListModel>
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var popupWindow: PopupWindow

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        hashMap = HashMap()
        list = ArrayList()

        popView = LayoutInflater.from(this).inflate(R.layout.pop_incoming_filter, null, false)
        etGys = popView.findViewById(R.id.et_gys)
        etWpmc = popView.findViewById(R.id.et_wpmc)
        tvStartTime = popView.findViewById(R.id.tv_start_time)
        tvEndTime = popView.findViewById(R.id.tv_end_time)
        tvStartTime clicks {
            startDate = DateUtil.chooseStartDate(mContext, tvStartTime, startDate, endDate)
        }
        tvEndTime clicks {
            endDate = DateUtil.chooseEndDate(mContext, tvEndTime, startDate, endDate)
        }
        popView.findViewById<Button>(R.id.btn_reset) clicks {
            etGys.setText("")
            etWpmc.setText("")
            tvStartTime.text = ""
            tvEndTime.text = ""
        }
        popView.findViewById<Button>(R.id.btn_search) clicks {
            page = 1
            getList()
            popupWindow.dismiss()
        }

        adapter = IncomingListAdapter(this, list)
        v.mRecyclerView.layoutManager = LinearLayoutManager(this)
        v.mRecyclerView.adapter = adapter

        getList()

        v.refreshLayout.setOnRefreshListener {
            page = 1
            getList()
        }
        v.refreshLayout.setOnLoadMoreListener {
            page++
            getList()
        }

    }

    private fun getList() {
        initMap()
        vm.getIncomingList(hashMap)
        v.refreshLayout.finishRefresh()
        v.refreshLayout.finishLoadMore()
    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["gysjc"] = etGys.text.toString()
        hashMap["wpmc"] = etWpmc.text.toString()
        hashMap["ksrq"] = tvStartTime.text.toString()
        hashMap["jsrq"] = tvEndTime.text.toString()
        hashMap["pagesize"] = "10"
        hashMap["pageindex"] = page.toString()
    }

    override fun initClick() {
        v.ivBack clicks { finish() }

        v.ivSearch clicks {
            popupWindow = PopupWindow(
                popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                isFocusable = true
                isOutsideTouchable = true
/*                animationStyle = R.style.anim_pop_filter*/
            }
            popupWindow.showAsDropDown(v.consTitle, 0, 1)
        }

        v.tvHistory clicks {
            startActivity(Intent(this, IncomingHistoryListActivity::class.java))
        }

    }

    override fun initData() {
    }

    override fun initVM() {
        vm.incominglist.observe(this) {
            if (page == 1) list.clear()
            it.let { it1 -> list.addAll(it1) }
            adapter.notifyDataSetChanged()
        }
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            page = 1
            getList()
        }
    }

}