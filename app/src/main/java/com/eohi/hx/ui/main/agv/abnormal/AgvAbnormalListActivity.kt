package com.eohi.hx.ui.main.agv.abnormal

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityAgvabnormalListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.agv.abnormal.adapter.AgvAbnormalListAdapter
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalListBean
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc:agv异常处理列表
 */
class AgvAbnormalListActivity :
    BaseActivity<AgvAbnormalViewModel, ActivityAgvabnormalListBinding>(),
    AgvAbnormalListAdapter.RefreshListener {

    private var page: Int = 1
    private lateinit var popView: View
    private lateinit var popupWindow: PopupWindow
    private lateinit var etCh: EditText
    private lateinit var etQd: EditText
    private lateinit var etZd: EditText
    private lateinit var etCjr: EditText
    private lateinit var scanCh: ImageView
    private lateinit var scanQd: ImageView
    private lateinit var scanZd: ImageView
    private lateinit var scanCjr: ImageView
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var rg: RadioGroup
    private lateinit var rbAbnormal: RadioButton
    private lateinit var rbAll: RadioButton
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var hashMap: HashMap<String, String>
    private lateinit var agvAbnormalList: ArrayList<AgvAbnormalListBean>
    private lateinit var adapter: AgvAbnormalListAdapter

    companion object {
        const val REQUEST_CH = 0x001
        const val REQUEST_QD = 0x002
        const val REQUEST_ZD = 0x003
        const val REQUEST_CJR = 0x004
    }

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        initPop()

        v.refreshLayout.setOnRefreshListener {
            page = 1
            getList()
        }
        v.refreshLayout.setOnLoadMoreListener {
            page++
            getList()
        }

        getList()

    }

    private fun initPop() {
        popView = LayoutInflater.from(this).inflate(R.layout.pop_agvabnormal_filter, null, false)
        etCh = popView.findViewById(R.id.et_ch)
        etQd = popView.findViewById(R.id.et_qd)
        etZd = popView.findViewById(R.id.et_zd)
        etCjr = popView.findViewById(R.id.et_cjr)
        scanCh = popView.findViewById(R.id.scan_ch)
        scanQd = popView.findViewById(R.id.scan_qd)
        scanZd = popView.findViewById(R.id.scan_zd)
        scanCjr = popView.findViewById(R.id.scan_cjr)
        tvStartTime = popView.findViewById(R.id.tv_start_time)
        tvEndTime = popView.findViewById(R.id.tv_end_time)
        rg = popView.findViewById(R.id.rg)
        rbAbnormal = popView.findViewById(R.id.rb_abnormal)
        rbAll = popView.findViewById(R.id.rb_all)
        scanCh clicks {
            checkCameraPermissions(REQUEST_CH)
        }
        scanQd clicks {
            checkCameraPermissions(REQUEST_QD)
        }
        scanZd clicks {
            checkCameraPermissions(REQUEST_ZD)
        }
        scanCjr clicks {
            checkCameraPermissions(REQUEST_CJR)
        }
        tvStartTime clicks {
            startDate = DateUtil.chooseStartDate(mContext, tvStartTime, startDate, endDate)
        }
        tvEndTime clicks {
            endDate = DateUtil.chooseEndDate(mContext, tvEndTime, startDate, endDate)
        }
        tvStartTime.text = DateUtil.lastWeek
        tvEndTime.text = DateUtil.audioTime
        popView.findViewById<Button>(R.id.btn_reset) clicks {
            etCh.setText("")
            etQd.setText("")
            etZd.setText("")
            etCjr.setText("")
            tvStartTime.text = DateUtil.lastWeek
            tvEndTime.text = DateUtil.audioTime
            rg.check(0)
        }
        popView.findViewById<Button>(R.id.btn_search) clicks {
            agvAbnormalList.clear()
            getList()
            popupWindow.dismiss()
        }
    }

    private fun getList() {
        initMap()
        if (rbAbnormal.isChecked) {
            vm.getAgvAbnormalList(hashMap)
        } else if (rbAll.isChecked) {
            vm.getAgvAbnormalAllList(hashMap)
        }
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
            }
            popupWindow.showAsDropDown(v.consTitle, 0, 1)
        }
    }

    override fun initData() {
        hashMap = HashMap()
        agvAbnormalList = ArrayList()
        adapter = AgvAbnormalListAdapter(this, agvAbnormalList)
        adapter.setRefreshListener(this)
        v.mRecyclerView.layoutManager = LinearLayoutManager(this)
        v.mRecyclerView.adapter = adapter
    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["ch"] = etCh.text.toString()
        hashMap["qd"] = etQd.text.toString()
        hashMap["zd"] = etZd.text.toString()
        hashMap["cjr"] = etCjr.text.toString()
        hashMap["kssj"] = tvStartTime.text.toString()
        hashMap["jssj"] = tvEndTime.text.toString()
        hashMap["pagesize"] = "10"
        hashMap["pageindex"] = page.toString()
    }

    override fun initVM() {
        vm.agvAbnormalList.observe(this) {
            if (page == 1) agvAbnormalList.clear()
            agvAbnormalList.addAll(it)
            adapter.notifyDataSetChanged()
        }
        vm.cancelResult.observe(this) {
            showShortToast(it[0].returntext)
            getList()
        }
    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions(requestCode: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, requestCode)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
            when (requestCode) {
                REQUEST_CH -> {
                    etCh.setText(result)
                }
                REQUEST_QD -> {
                    etQd.setText(result)
                }
                REQUEST_ZD -> {
                    etZd.setText(result)
                }
                REQUEST_CJR -> {
                    etCjr.setText(result)
                }
            }
        }
    }

    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when {
                    etQd.isFocused -> {
                        etQd.setText(result)
                    }
                    etZd.isFocused -> {
                        etZd.setText(result)
                    }
                    etCh.isFocused -> {
                        etCh.setText(result)
                    }
                    etCjr.isFocused -> {
                        etCjr.setText(result)
                    }
                }
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            page = 1
            getList()
        }
    }

    override fun refresh(rwid: String) {
        vm.cancel(rwid, accout)
    }

}