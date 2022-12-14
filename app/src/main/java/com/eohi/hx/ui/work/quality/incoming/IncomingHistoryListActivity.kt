package com.eohi.hx.ui.work.quality.incoming

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityIncomingHistoryListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.IncomingHistoryListAdapter
import com.eohi.hx.ui.work.model.IncomingListModel
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogPrompt
import com.eohi.hx.widget.clicks
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class IncomingHistoryListActivity :
    BaseActivity<IncomingViewModel, ActivityIncomingHistoryListBinding>() {

    private lateinit var adapter: IncomingHistoryListAdapter
    private lateinit var list: ArrayList<IncomingListModel>
    private lateinit var hashMap: HashMap<String, String>
    private var page: Int = 1
    private lateinit var popView: View
    private lateinit var etGys: EditText
    private lateinit var etWpmc: EditText
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var popupWindow: PopupWindow

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

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
            when {
                TextUtils.isEmpty(tvStartTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                else -> {
                    list.clear()
                    getList()
                    popupWindow.dismiss()
                }
            }
        }

        list = ArrayList()
        hashMap = HashMap()
        adapter = IncomingHistoryListAdapter(this, list)
        v.mRecyclerView.layoutManager = LinearLayoutManager(this)
        v.mRecyclerView.adapter = adapter

        adapter.itemLongClick { view, i ->
            val dialogPrompt = DialogPrompt(this)
            dialogPrompt.setOnClickListener(object : DialogPrompt.OnClickListener {
                override fun onPositiveClick() {
                    dialogPrompt.dismiss()
                    vm.delete(list[i].RWDH)
                    list.removeAt(i)
                }
            })
            dialogPrompt.show()
        }

        v.refreshLayout.setOnRefreshListener {
            when {
                TextUtils.isEmpty(tvStartTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                else -> {
                    page = 1
                    getList()
                }
            }
        }
        v.refreshLayout.setOnLoadMoreListener {
            when {
                TextUtils.isEmpty(tvStartTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("?????????????????????")
                    v.refreshLayout.finishRefresh()
                }
                else -> {
                    page++
                    getList()
                }
            }
        }
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

    private fun getList() {
        initMap()
        vm.getIncomingHistoryList(hashMap)
        v.refreshLayout.finishRefresh()
        v.refreshLayout.finishLoadMore()
    }

    override fun initClick() {
        v.ivBack clicks { finish() }

        v.ivSearch clicks {
            popupWindow = PopupWindow(
                popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                isTouchable = true
                isFocusable = true
                isOutsideTouchable = true
/*                animationStyle = R.style.anim_pop_filter*/
            }
            popupWindow.showAsDropDown(v.consTitle, 0, 1)
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
        vm.deleteInComing.observe(this) {
            Toast.makeText(mContext, it.fhxx, Toast.LENGTH_SHORT).show()
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