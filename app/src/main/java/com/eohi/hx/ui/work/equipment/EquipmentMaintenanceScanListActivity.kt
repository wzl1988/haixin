package com.eohi.hx.ui.work.equipment

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentMaintenanceScanListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.EquipmentMaintenanceScanListAdapter
import com.eohi.hx.ui.work.equipment.model.TaskSBLBss
import com.eohi.hx.utils.Constant
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
import kotlinx.android.synthetic.main.activity_equipment_maintenance_scan_list.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * 设备维保列表 扫码
 */
class EquipmentMaintenanceScanListActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentMaintenanceScanListBinding>() {
    var adapter: EquipmentMaintenanceScanListAdapter? = null
    var listDatas: ArrayList<TaskSBLBss>? = null
    lateinit var templist:ArrayList<TaskSBLBss>
    private var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        listDatas = ArrayList()
        adapter = EquipmentMaintenanceScanListAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        adapter!!.itemClick {
           val intent =  Intent(mContext,EquipmentMaintenanceListActivity::class.java)
            intent.putExtra("sbbh",
                listDatas!![it].SBBH)
            intent.putExtra("azdd", listDatas!![it].SBDQSZWZ)
            intent.putExtra("csh", listDatas!![it].CSH)
            intent.putExtra("csmc", listDatas!![it].CSMC)
            startActivity(intent)
        }

        setTaskSBLBss(true, null)
    }

    fun setTaskSBLBss(iss: Boolean, text: String?) {
        if (iss) {
            RetrofitUtil.builder.baseUrl(apiurl)
                .build()
                .create(ApiService::class.java)
                .setTaskSBLBss(accout)?.enqueue(myCall())
        } else {
            RetrofitUtil.builder.baseUrl(apiurl)
                .build()
                .create(ApiService::class.java)
                .setTaskSBLBss(accout, text!!)?.enqueue(myCall())
        }
    }

    fun myCall(): MyCallBack<FatherList<TaskSBLBss>> {
        return object : MyCallBack<FatherList<TaskSBLBss>>() {
            override fun success(t: FatherList<TaskSBLBss>?) {
                v.refreshLayout.finishRefresh()
                listDatas?.clear()
                listDatas?.addAll(t?.list!!)
                templist = listDatas?.clone() as ArrayList<TaskSBLBss>
                adapter?.notifyDataSetChanged()
                v.tvScxx.text = "当前待维保设备数："+t?.list?.size
            }

            override fun failure(apiErrorModel: ApiErrorModel?) {
                print("sb")
            }
        }
    }

    override fun initClick() {
        btn_scgx.setOnClickListener {
            if (TextUtils.isEmpty(et_scgx.text.toString())) {
                listDatas?.clear()
                listDatas?.addAll(templist!!)
                adapter?.notifyDataSetChanged()
                return@setOnClickListener
            }
            var list=templist?.filter {
                it.toString().contains(et_scgx.text.toString())
            }
            listDatas?.clear()
            listDatas?.addAll(list!!)
            adapter?.notifyDataSetChanged()

            //api模糊查询
//            setTaskSBLBss(false, et_scgx.text.toString())
        }
//        et_scgx.setOnClickListener {
//            checkCameraPermissions()
//        }
        iv_back.clicks {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    var result = data.getStringExtra(Intents.Scan.RESULT)
                    et_scgx.setText(result)
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


    override fun handleEvent(msg: EventMessage) {
        super.handleEvent(msg)
        when(msg.code){
            EventCode.REFRESH->{
                setTaskSBLBss(true, null)
            }
        }
    }

    override fun initData() {
        v.refreshLayout.setOnRefreshListener {
            setTaskSBLBss(true, null)
        }
    }

    override fun initVM() {
    }
}