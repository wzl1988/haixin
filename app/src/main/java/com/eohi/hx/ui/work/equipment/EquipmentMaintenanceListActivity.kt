package com.eohi.hx.ui.work.equipment

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentMaintenanceListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.EquipmentMaintenanceListAdapter
import com.eohi.hx.ui.work.equipment.model.TaskPartModel
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.view.SpacesItemDecoration
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.activity_equipment_maintenance_scan_list.*

/**
 * 设备维保列表 第二阶段
 */
class EquipmentMaintenanceListActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentMaintenanceListBinding>() {
    var adapter: EquipmentMaintenanceListAdapter? = null
    var listDatas: ArrayList<TaskPartModel>? = null
    private var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        val dd= intent.getStringExtra("azdd")
        val csh = intent.getStringExtra("csh")
        val csmc = intent.getStringExtra("csmc")
        listDatas = ArrayList()
        adapter = EquipmentMaintenanceListAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        adapter!!.itemClick {
            val intent = Intent(mContext,EquipmentMaintenanceActivity::class.java)
            val data= listDatas!![it]
            intent.putExtra("sbbh",data.SBBH)
            intent.putExtra("BJEWM", data.BJEWM)
            intent.putExtra("sbmc",data.SBMC)
            intent.putExtra("xh",data.XH)
            intent.putExtra("bjh",data.WBBJBH)
            intent.putExtra("bjmc",data.WBBJMC)
            intent.putExtra("dd",dd)
            intent.putExtra("wbrq",data.SCWBRQ)
            intent.putExtra("csh",csh)
            intent.putExtra("csmc",csmc)
            intent.putExtra("GSH",data.GSH)
            startActivity(intent)
        }
        setTask()
    }

    fun setTask() {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .getTaskPartlist(accout, intent.getStringExtra("sbbh"))
            ?.enqueue(object : MyCallBack<FatherList<TaskPartModel>>() {
                override fun success(t: FatherList<TaskPartModel>?) {
                    listDatas?.clear()
                    listDatas?.addAll(t?.list!!)
                    adapter?.notifyDataSetChanged()
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    override fun initClick() {
        iv_back.clicks {
            finish()
        }
    }

    override fun handleEvent(msg: EventMessage) {
        super.handleEvent(msg)
        when(msg.code){
            EventCode.REFRESH->{
                setTask()
            }
        }
    }

    override fun initData() {
    }

    override fun initVM() {
    }
}