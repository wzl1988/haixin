package com.eohi.hx.ui.work.equipment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentMaintenanceBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.EquipmentMaintenanceProjectAdapter
import com.eohi.hx.ui.work.equipment.model.Item
import com.eohi.hx.ui.work.equipment.model.MaintenannceProject
import com.eohi.hx.ui.work.equipment.model.MaintenannceSubmitmodel
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.view.SpacesItemDecoration
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.activity_equipment_maintenance.*
import org.greenrobot.eventbus.EventBus

/**
 *  设备维保处理
 */
class EquipmentMaintenanceActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentMaintenanceBinding>() {
    private var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    private var companyname by Preference("companyname", "")
    var adapter: EquipmentMaintenanceProjectAdapter? = null
    var listDatas: ArrayList<MaintenannceProject>? = null
    lateinit var kswbsj:String
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        kswbsj = DateUtil.audioTime
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.etLzkkh.text = intent.getStringExtra("sbbh")
        v.tvSbmc.text = intent.getStringExtra("sbmc")
        v.tvGg.text = intent.getStringExtra("xh")
        v.tvBjh.text = intent.getStringExtra("bjh")
        v.tvBjmc.text = intent.getStringExtra("bjmc")
        v.tvAzdd.text = intent.getStringExtra("dd")
        v.tvWbsj.text = intent.getStringExtra("wbrq")

    }

    override fun initClick() {
        iv_back.clicks { finish() }
        btn_submit.clicks {
            var listitem = ArrayList<Item>()
            listDatas?.forEach {
                var model = Item(it.JHDH,it.WBXMBH
                ,it.WBXMLB,it.WBXMMC,it.wcqk)
                listitem.add(model)
            }
            var model =MaintenannceSubmitmodel(intent.getStringExtra("BJEWM")
                ,"",intent.getStringExtra("csh"),
                intent.getStringExtra("csmc"), intent.getStringExtra("GSH"),companyname,listitem,
            DateUtil.audioTime,kswbsj,intent.getStringExtra("sbbh")
            ,intent.getStringExtra("sbmc"),intent.getStringExtra("wbrq"),
            "","", accout,intent.getStringExtra("BJEWM"),0,0)
            submit(model)

        }
    }

    override fun initData() {
        v.tvCzy.text = "操作员：$username"
        v.tvRq.text = "日期："+DateUtil.data
        listDatas = ArrayList()
        adapter = EquipmentMaintenanceProjectAdapter(this, listDatas!!)
        v.rc.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        }
        adapter?.setOncheced { i, s ->
            listDatas!![i]!!.wcqk =s
        }
        getWbList(intent.getStringExtra("BJEWM"),intent.getStringExtra("sbbh"))
    }

    override fun initVM() {
    }

    fun getWbList(bjewm:String,sbbh:String){
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .getMaintenanceProject(bjewm,sbbh)?.enqueue(object:MyCallBack<FatherList<MaintenannceProject>>(){
                override fun success(t: FatherList<MaintenannceProject>?) {
                    t?.list?.let { listDatas?.addAll(it) }
                    adapter?.notifyDataSetChanged()
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {

                }
            })
    }

    fun submit(model: MaintenannceSubmitmodel){
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .submitMaintenance(model).enqueue(object : MyCallBack<Any>() {
                override fun success(t: Any) {
                    EventBus.getDefault().post(EventMessage(EventCode.REFRESH))
                    finish()
                    showShortToast("提交成功")
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }

            })
    }

}