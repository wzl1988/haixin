package com.eohi.hx.ui.work.equipment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Html
import android.text.TextUtils
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentCheckBinding
import com.eohi.hx.ui.work.adapter.EquipmentCheck2Adapter
import com.eohi.hx.ui.work.equipment.model.*
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.view.SpacesItemDecoration
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import kotlinx.android.synthetic.main.activity_equipment_check.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * 日常点检
 */
class EquipmentCheckActivity : BaseActivity<BaseViewModel, ActivityEquipmentCheckBinding>() {
    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    var adapter: EquipmentCheck2Adapter? = null
    var listDatas: ArrayList<EquipmentParts>? = null
    private var dailyCheck = DailyCheck()
    private lateinit var dailyCheckItem: DailyCheckItem
    private lateinit var dailyCheckSubItem: DailyCheckSubItem
    private lateinit var sbbh: String
    private lateinit var sbmc: String
    private lateinit var sbscrbsj: String
    private lateinit var csh: String
    private lateinit var csmc: String

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        listDatas = ArrayList()
        adapter = EquipmentCheck2Adapter(mContext, listDatas!!, ::onCheck)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 0))
    }

    private fun onCheck(i: Int, i1: Int, s: String) {
        listDatas?.get(i)?.item?.get(i1)?.djjg = s
    }

    fun getCheckList(barcode: String) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .getCheckList(barcode).enqueue(object : MyCallBack<FatherList<EquipmentCheck>>() {
                override fun success(t: FatherList<EquipmentCheck>?) {
                    sbbh = t?.list?.get(0)?.SBBM.toString()
                    sbmc = t?.list?.get(0)?.SBMC.toString()
                    sbscrbsj = t?.list?.get(0)?.lastdjtime.toString()
                    csh = t?.list?.get(0)?.CSH.toString()
                    csmc = t?.list?.get(0)?.CSMC.toString()

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tv_wpmc_text.text = Html.fromHtml(
                            "<font color='#333333'>设备名称：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBMC + "</font>", Html.FROM_HTML_MODE_LEGACY
                        )
                        tv_gg_text.text = Html.fromHtml(
                            "<font color='#333333'>规格型号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBXH + "</font>", Html.FROM_HTML_MODE_LEGACY
                        )
                        tc_azwz.text = Html.fromHtml(
                            "<font color='#333333'>安装地点：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.DQSZWZ + "</font>",
                            Html.FROM_HTML_MODE_LEGACY
                        )
                        tc_scrj.text = Html.fromHtml(
                            "<font color='#333333'>上次日检：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.lastdjtime + "</font>",
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        tv_wpmc_text.text = Html.fromHtml(
                            "<font color='#333333'>设备名称:</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBMC + "</font>"
                        )
                        tv_gg_text.text = Html.fromHtml(
                            "<font color='#333333'>规格型号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBXH + "</font>"
                        )
                        tc_azwz.text = Html.fromHtml(
                            "<font color='#333333'>安装地点：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.DQSZWZ + "</font>"
                        )
                        tc_scrj.text = Html.fromHtml(
                            "<font color='#333333'>上次日检：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.lastdjtime + "</font>"
                        )
                    }
                    listDatas?.addAll(t?.list?.get(0)!!.item)
                    adapter?.notifyDataSetChanged()

                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    private fun setCheckData(lzkPostModel: DailyCheck) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setCheckData(lzkPostModel).enqueue(object : MyCallBack<Any>() {
                override fun failure(apiErrorModel: ApiErrorModel?) {
                }

                override fun success(t: Any?) {
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_LONG).show()
                    finish()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.etLzkkh.setText(result)
                    getCheckList(result)
                }
            }
        }
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun initClick() {
        v.etLzkkh.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                actionId == EditorInfo.IME_ACTION_DONE || event != null
                && KeyEvent.KEYCODE_ENTER == event.keyCode
                && KeyEvent.ACTION_DOWN == event.action
            ) {
                getCheckList(v.etLzkkh.text.toString())
            }
            false
        }
        v.ivBack clicks { finish() }
        v.etLzkkh.setOnClickListener {
            checkCameraPermissions()
        }
        v.btnPost.setOnClickListener {
            if (TextUtils.isEmpty(et_lzkkh.text.toString())) {
                Toast.makeText(this, "请先扫码！", Toast.LENGTH_SHORT).show()
            } else {
                listDatas?.onEach { it ->
                    dailyCheckItem = DailyCheckItem()
                    dailyCheckItem.djbwbh = it.BJBH
                    dailyCheckItem.djbwmc = it.BJMC
                    it.item.onEach {
                        if (it.djjg == null) {
                            Toast.makeText(this, "请选择点检结果", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        dailyCheckSubItem = DailyCheckSubItem()
                        dailyCheckSubItem.djjg = it.djjg
                        dailyCheckSubItem.djxmmc = it.equName
                        dailyCheckSubItem.djxmbh = it.equCode
                        dailyCheckItem.item.add(dailyCheckSubItem)
                    }
                    dailyCheck.item.add(dailyCheckItem)
                }
                dailyCheck.gsh = companyNo
                dailyCheck.gsmc = companyname
                dailyCheck.csh = csh
                dailyCheck.csmc = csmc
                dailyCheck.userid = accout
                dailyCheck.sbbh = sbbh
                dailyCheck.sbmc = sbmc
                dailyCheck.sbscrbsj = sbscrbsj
                setCheckData(dailyCheck)
            }
        }
    }

    override fun initData() {
        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime
    }

    override fun initVM() {
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
                v.etLzkkh.setText(result)
                getCheckList(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}