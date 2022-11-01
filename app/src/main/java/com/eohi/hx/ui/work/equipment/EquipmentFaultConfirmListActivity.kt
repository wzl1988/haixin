package com.eohi.hx.ui.work.equipment

import android.content.Intent
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentFaultConfirmListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.EquipmentFaultConfirmListAdapter
import com.eohi.hx.ui.work.equipment.model.Fault
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.view.SpacesItemDecoration
import com.eohi.hx.widget.clicks


/**
 * 故障确认
 */
class EquipmentFaultConfirmListActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentFaultConfirmListBinding>() {

    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    var adapter: EquipmentFaultConfirmListAdapter? = null
    var listDatas: ArrayList<Fault>? = null

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        listDatas = ArrayList()
        adapter = EquipmentFaultConfirmListAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        adapter!!.itemClick {
            startActivity(
                Intent(mContext, EquipmentFaultConfirmActivity::class.java).putExtra(
                    "sbbh",
                    listDatas!![it].SBBH
                )
                    .putExtra("SBMC", listDatas!![it].SBMC)
                    .putExtra("SBBJBH", listDatas!![it].SBBJBH)
                    .putExtra("XH", listDatas!![it].XH)
                    .putExtra("SBDQSZWZ", listDatas!![it].SBDQSZWZ)
                    .putExtra("PJSWH", listDatas!![it].PJSWH)
                    .putExtra("GZBM",listDatas!![it].GZBM)
                    .putExtra("GZMC",listDatas!![it].GZMC)
                    .putExtra("XDWCGS",listDatas!![it].XDWCGS)
                    .putExtra("JJCD",listDatas!![it].JJCD)
                    .putExtra("GZQK",listDatas!![it].GZQK)
            )
        }
        setTaskFaultList()
    }

    private fun setTaskFaultList() {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setTaskFaultList(accout).enqueue(object : MyCallBack<FatherList<Fault>>() {
                override fun success(t: FatherList<Fault>?) {
                    v.refreshLayout.finishRefresh()
                    listDatas?.clear()
                    listDatas?.addAll(t?.list!!)
                    adapter?.notifyDataSetChanged()
                    v.tvScxx.text ="当前待确认记录数："+listDatas?.size
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.btnScgx clicks {
            if (TextUtils.isEmpty(v.etScgx.text.toString())) {
                setTaskFaultList()
            } else {
                val list = listDatas?.filter { it.SBBH.contains(v.etScgx.text.toString()) }
                listDatas?.clear()
                listDatas?.addAll(list!!)
                adapter?.notifyDataSetChanged()
                v.tvScxx.text ="当前待确认记录数："+listDatas?.size
            }
        }
    }

    override fun handleEvent(msg: EventMessage) {
        super.handleEvent(msg)
        when(msg.code){
            EventCode.REFRESH->{
                setTaskFaultList()
            }
        }
    }


    override fun initData() {
        v.refreshLayout.setOnRefreshListener {
            setTaskFaultList()
        }
    }

    override fun initVM() {
    }
}