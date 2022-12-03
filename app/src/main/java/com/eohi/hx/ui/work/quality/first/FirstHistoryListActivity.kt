package com.eohi.hx.ui.work.quality.first

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityFirstHistoryListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.FirstHistoryListAdapter
import com.eohi.hx.ui.work.model.FirstCheckListResult
import com.eohi.hx.ui.work.model.JylxBean
import com.eohi.hx.ui.work.model.OldFirstCheckListResult
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogPrompt
import com.eohi.hx.view.MySpinnerAdapter
import com.eohi.hx.widget.clicks
import java.util.*

//不启用
class FirstHistoryListActivity :
    BaseActivity<FirstCheckViewModel, ActivityFirstHistoryListBinding>() {

    private lateinit var hashMap: HashMap<String, String>
    private var page: Int = 1
    private lateinit var adapter: FirstHistoryListAdapter
    private lateinit var list: ArrayList<OldFirstCheckListResult>
    private lateinit var popView: View
    private lateinit var etGdh: EditText
    private lateinit var etWpmc: EditText
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var popupWindow: PopupWindow
    private lateinit var spJylx: Spinner
    private var jylx = ""
    private var jylxList = ArrayList<JylxBean>()
    private var jylxMcList = ArrayList<String>()
    private lateinit var jylxAdapter: ArrayAdapter<String>

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        list = ArrayList()
        hashMap = HashMap()

        popView = LayoutInflater.from(this).inflate(R.layout.pop_first_filter, null, false)
        etGdh = popView.findViewById(R.id.et_gdh)
        etWpmc = popView.findViewById(R.id.et_wpmc)
        tvStartTime = popView.findViewById(R.id.tv_start_time)
        tvEndTime = popView.findViewById(R.id.tv_end_time)
        spJylx = popView.findViewById(R.id.sp_jylx)
        jylxAdapter = MySpinnerAdapter(this, android.R.layout.simple_spinner_item, jylxMcList)
        spJylx.adapter = jylxAdapter
        spJylx.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    return
                } else {
                    jylx = jylxList[position - 1].xmfldm
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        tvStartTime clicks {
            startDate = DateUtil.chooseStartDate(mContext, tvStartTime, startDate, endDate)
        }
        tvEndTime clicks {
            endDate = DateUtil.chooseEndDate(mContext, tvEndTime, startDate, endDate)
        }
        popView.findViewById<Button>(R.id.btn_reset) clicks {
            etGdh.setText("")
            etWpmc.setText("")
            tvStartTime.text = ""
            tvEndTime.text = ""
        }
        popView.findViewById<Button>(R.id.btn_search) clicks {
            when {
                TextUtils.isEmpty(tvStartTime.text.toString()) -> {
                    showShortToast("请选择开始时间")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("请选择结束时间")
                    v.refreshLayout.finishRefresh()
                }
                else -> {
                    page = 1
                    getList()
                    popupWindow.dismiss()
                }
            }
        }

        vm.getJylx("45")

        adapter = FirstHistoryListAdapter(this, list)
        v.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        v.mRecyclerView.adapter = adapter

        adapter.itemLongClick { view, i ->
            val dialogPrompt = DialogPrompt(this)
            dialogPrompt.setOnClickListener(object : DialogPrompt.OnClickListener {
                override fun onPositiveClick() {
                    dialogPrompt.dismiss()
                    vm.delete(list[i].RWBH)
                    list.removeAt(i)
                }
            })
            dialogPrompt.show()
        }

        v.refreshLayout.setOnRefreshListener {
            when {
                TextUtils.isEmpty(tvStartTime.text.toString()) -> {
                    showShortToast("请选择开始时间")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("请选择结束时间")
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
                    showShortToast("请选择开始时间")
                    v.refreshLayout.finishRefresh()
                }
                TextUtils.isEmpty(tvEndTime.text.toString()) -> {
                    showShortToast("请选择结束时间")
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
        hashMap["pageindex"] = page.toString()
        hashMap["pagesize"] = "10"
        hashMap["gsh"] = companyNo
        hashMap["ksrq"] = tvStartTime.text.toString()
        hashMap["jsrq"] = tvEndTime.text.toString()
        hashMap["wpmc"] = etWpmc.text.toString()
        hashMap["scgdh"] = etGdh.text.toString()
        hashMap["jylxm"] = jylx
    }

    private fun getList() {
        initMap()
        vm.getFirstCheckResultList(hashMap)
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
        vm.odlchecklist.observe(this) {
            if (page == 1) list.clear()
            it.let { it1 -> list.addAll(it1) }
            adapter.notifyDataSetChanged()
        }
        vm.deleteFirstCheck.observe(this) {
            Toast.makeText(mContext, it.fhxx, Toast.LENGTH_SHORT).show()
            adapter.notifyDataSetChanged()
        }
        vm.jylxBean.observe(this) {
            jylxList.addAll(it)
            jylxList.forEach { jylxBean ->
                jylxMcList.add(jylxBean.xmflmc)
            }
        }
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            page = 1
            getList()
        }
    }

}