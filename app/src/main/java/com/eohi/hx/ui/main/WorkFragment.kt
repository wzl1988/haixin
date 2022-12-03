package com.eohi.hx.ui.main

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.FragmentWorkNewBinding
import com.eohi.hx.ui.main.model.EquipNumModel
import com.eohi.hx.ui.main.model.Menu
import com.eohi.hx.ui.main.model.Secondcd
import com.eohi.hx.ui.main.model.Threecd
import com.eohi.hx.ui.work.adapter.ImageViewAdapter
import com.eohi.hx.ui.work.equipment.*
import com.eohi.hx.ui.work.inventory.InventoryAdjustmentActivity
import com.eohi.hx.ui.work.model.ImageViewModel
import com.eohi.hx.ui.work.mould.MouldInActivity
import com.eohi.hx.ui.work.mould.MouldOutActivity
import com.eohi.hx.ui.work.move.OrdinaryMoveActivity
import com.eohi.hx.ui.work.my.ProcessingUnitActivity
import com.eohi.hx.ui.work.my.ProductionTeamActivity
import com.eohi.hx.ui.work.picking.move.PickingMoveActivity
import com.eohi.hx.ui.work.picking.outbound.PickingOutboundActivity
import com.eohi.hx.ui.work.platform.PlatformFreeActivity
import com.eohi.hx.ui.work.process.card.CirculationCardActivity
import com.eohi.hx.ui.work.process.card.CirculationCardDetailActivity
import com.eohi.hx.ui.work.process.coordination.CoordinationFinishedActivity
import com.eohi.hx.ui.work.process.coordination.CoordinationStartActivity
import com.eohi.hx.ui.work.process.material.MaterialRemovalActivity
import com.eohi.hx.ui.work.process.registration.ProductionRegistrationActivity
import com.eohi.hx.ui.work.process.start.StartWorkActivity
import com.eohi.hx.ui.work.purchasein.FinishingInActivity
import com.eohi.hx.ui.work.purchasein.InstorageActivity
import com.eohi.hx.ui.work.purchasein.retreat.ProcurementRetreatActivity
import com.eohi.hx.ui.work.quality.delivery.DeliveryListActivity
import com.eohi.hx.ui.work.quality.finish.FinishCheckActivity
import com.eohi.hx.ui.work.quality.finish.FinishListActivity
import com.eohi.hx.ui.work.quality.first.FirstCheckListActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingListActivity
import com.eohi.hx.ui.work.quality.process.ProcessCheckActivity
import com.eohi.hx.ui.work.quality.process.ProcessListActivity
import com.eohi.hx.ui.work.quality.register.InspectionRegisterActivity
import com.eohi.hx.ui.work.quality.rejects.RejectsListActivity
import com.eohi.hx.ui.work.quality.unqualified.UnQualifiedReportActivity
import com.eohi.hx.ui.work.sales.delivery.SalesDeliveryOutActivity
import com.eohi.hx.ui.work.sales.retreat.SalesRetreatActivity
import com.eohi.hx.ui.work.tooling.back.ToolingBackActivity
import com.eohi.hx.ui.work.tooling.check.ToolingCheck
import com.eohi.hx.ui.work.tooling.cutter.CutterReplace
import com.eohi.hx.ui.work.tooling.handover.ToolingHandover
import com.eohi.hx.ui.work.tooling.maintenance.ToolingMaintenance
import com.eohi.hx.ui.work.tooling.onlineoffline.ToolOnlineOfflineActivity
import com.eohi.hx.ui.work.tooling.recipients.ToolingRecipientsActivity
import com.eohi.hx.ui.work.tooling.recipients.ToolingRecipientsList
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/9 17:29
 */
class WorkFragment : BaseFragment<BaseViewModel, FragmentWorkNewBinding>() {

    private var firstmenus: ArrayList<Menu>? by Preference("menus", null)
    lateinit var workmenus: ArrayList<Secondcd>
    var pointResult = MutableLiveData<ArrayList<EquipNumModel>>()
    val list = ArrayList<ImageViewModel>()
    val zllist = ArrayList<ImageViewModel>()
    lateinit var zzpadapter:ImageViewAdapter
    lateinit var zladpater:ImageViewAdapter
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        pointResult.observe(this, Observer { point ->
            if(list.isNotEmpty()){
                point.forEach { pointlist ->
                    for (i in list.indices) {
                        if (pointlist.name == "不良品列表" && list[i].str == "不良品判定") {
                            list[i].number = pointlist.count
                        }
                    }

                    for (index in zllist.indices){
                        if (pointlist.name == "首件检验" && zllist[index].str == "首件检验") {
                            zllist[index].number = pointlist.count
                            zladpater.notifyItemChanged(index)
                            break
                        }
                    }
                }

                zzpadapter= ImageViewAdapter(mContext, list)
                v.rcZzp.let {
                    it.layoutManager = GridLayoutManager(mContext, 4)
                    it.adapter = zzpadapter
                }

                zzpadapter.onNewItemClick {image,url->
                    val intent = Intent()
                    intent.putExtra("conmap",url)
                    when (image) {
                        R.mipmap.work_gxwx -> {
                            intent.setClass(mContext, CoordinationStartActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_wg -> {
                            intent.setClass(mContext, CoordinationFinishedActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_clck -> {
                            intent.setClass(mContext, MaterialRemovalActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_kg -> {
                            intent.setClass(mContext, StartWorkActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_lzkxq -> {
                            intent.setClass(mContext, CirculationCardDetailActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_lzkck -> {
                            intent.setClass(mContext, CirculationCardActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_scdj -> {
                            intent.setClass(mContext, ProductionRegistrationActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_zl_bhgpdj -> {
                            intent.setClass(mContext, UnQualifiedReportActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_djylb->{
                            intent.setClass(mContext, RejectsListActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

            }


        })
    }

    override fun initView() {
        for (i in firstmenus!!.indices) {
            if (firstmenus!![i].cdbh == "D01") {
                workmenus = (firstmenus!![i].secondcd as ArrayList<Secondcd>?)!!
                break
            }
        }

        var sm: ArrayList<Threecd>? = null
        var sb: ArrayList<Threecd>? = null
        var mj: ArrayList<Threecd>? = null
        var gz: ArrayList<Threecd>? = null
        var zzp: ArrayList<Threecd>? = null
        var zlgl: ArrayList<Threecd>? = null
        var my: ArrayList<Threecd>? = null

        var smUserd = false
        var mjUsed = false
        var gzUsed = false
        var sbUsed = true
        var zzpUsed = true
        var zlglUsed = true
        var myUsered = true

        try {
            for (i in workmenus.indices) {
                when (workmenus[i].cdbh1) {
                    "D0101" -> {
                        sm = workmenus[i].threecd as ArrayList<Threecd>
                        smUserd = workmenus[i].ifyqx1 == 1
                    }
                    "D0102" -> {
                        mj = workmenus[i].threecd as ArrayList<Threecd>
                        mjUsed = workmenus[i].ifyqx1 == 1
                    }
                    "D0103" -> {
                        gz = workmenus[i].threecd as ArrayList<Threecd>
                        gzUsed = workmenus[i].ifyqx1 == 1
                    }
                    "D0104" -> {
                        zzp = workmenus[i].threecd as ArrayList<Threecd>
                        zzpUsed = workmenus[i].ifyqx1 == 1
                    }
                    "D0105" -> {
                        sb = workmenus[i].threecd as ArrayList<Threecd>
                        sbUsed = workmenus[i].ifyqx1 == 1
                    }
                    "D0106" -> {
                        zlgl = workmenus[i].threecd as ArrayList<Threecd>
                        zlglUsed = workmenus[i].ifyqx1 == 1
                    }
                    "D0199" -> {
                        my = workmenus[i].threecd as ArrayList<Threecd>
                        myUsered = workmenus[i].ifyqx1 == 1
                    }
                }
            }

            //质量管理
            if (zlglUsed) {
                initZlgl(zlgl)
            } else {
                v.llZlgl.gone()
            }

            //设备
            if (sbUsed) {
                initSb(sb)
            } else {
                v.llSb.visibility = View.GONE
            }
            //扫码出入库
            if (smUserd) {
                initSmcrk(sm!!)
            } else {
                v.llCrk.visibility = View.GONE
            }

            //模具领用与归还
            if (mjUsed) {
                initMj(mj!!)
            } else {
                v.llMj.visibility = View.GONE
            }

            //工装管理
            if (gzUsed) {
                initGz(gz!!)
            } else {
                v.llGz.visibility = View.GONE
            }

            //在制品编组
            if (zzpUsed) {
                initZzp(zzp)
            } else {
                v.consZzp.visibility = View.GONE
            }
            if (myUsered) {
                initMy(my!!)
            } else {
                v.llMy.visibility = View.GONE
            }

        } catch (e: Exception) {
            ToastUtil.showToast(mContext, "权限配置出错")
        }

    }

    //在制品编组
    private fun initZzp(zzp: ArrayList<Threecd>?) {

        Log.i("zlgl",zzp.toString())
        for (i in zzp!!.indices) {
            when (zzp[i].cdbh2) {
                "D010401" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gxwx, "工序外协\n开工",zzp[i].ftpdjmc))
                }
                "D010402" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_wg, "工序外协\n完工",zzp[i].ftpdjmc))
                }
                "D010403" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_clck, "材料拆卡",zzp[i].ftpdjmc))
                }
                "D010404" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_kg, "开工",zzp[i].ftpdjmc))
                }
                "D010405" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_lzkxq, "流转卡详情",zzp[i].ftpdjmc))
                }
                "D010406" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_lzkck, "流转卡拆卡",zzp[i].ftpdjmc))
                }
                "D010407" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_scdj, "生产登记",zzp[i].ftpdjmc))
                }
                "D010408" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_zl_bhgpdj, zzp[i].cdmc2,zzp[i].ftpdjmc))
                }
                "D010410" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_djylb, zzp[i].cdmc2,zzp[i].ftpdjmc))
                }
            }
        }
        zzpadapter= ImageViewAdapter(mContext, list)
        v.rcZzp.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = zzpadapter
        }

        zzpadapter.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image) {
                R.mipmap.work_gxwx -> {
                    intent.setClass(mContext, CoordinationStartActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_wg -> {
                    intent.setClass(mContext, CoordinationFinishedActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_clck -> {
                    intent.setClass(mContext, MaterialRemovalActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_kg -> {
                    intent.setClass(mContext, StartWorkActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_lzkxq -> {
                    intent.setClass(mContext, CirculationCardDetailActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_lzkck -> {
                    intent.setClass(mContext, CirculationCardActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_scdj -> {
                    intent.setClass(mContext, ProductionRegistrationActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_zl_bhgpdj -> {
                    intent.setClass(mContext, UnQualifiedReportActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_djylb->{
                    intent.setClass(mContext, RejectsListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //质量管理
    private fun initZlgl(zlgl: ArrayList<Threecd>?) {

        for (i in zlgl!!.indices) {
            when (zlgl[i].cdbh2) {
                "D010601" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_lljy, "来料检验",zlgl[i].ftpdjmc))
                }
                "D010602" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_sjjy, "首件检验",zlgl[i].ftpdjmc))
                }
                "D010603" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_gcyj, "过程检验",zlgl[i].ftpdjmc))
                }
                "D010604" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_wgjy, "完工检验",zlgl[i].ftpdjmc))
                }
                "D010605" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_fhjy, "发货检验",zlgl[i].ftpdjmc))
                }
            }
        }


        zllist.add(ImageViewModel(R.mipmap.work_sjdj, "送检登记",""))

        zladpater = ImageViewAdapter(mContext, zllist)
        v.rcZlgl.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = zladpater
        }
        zladpater.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image) {
                R.mipmap.work_zl_lljy -> {
                    intent.setClass(mContext, IncomingListActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_zl_sjjy -> {
                    intent.setClass(mContext, FirstCheckListActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_zl_gcyj -> {
                    intent.setClass(mContext, ProcessCheckActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_zl_wgjy -> {
                    intent.setClass(mContext, FinishCheckActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_zl_fhjy -> {
                    intent.setClass(mContext, DeliveryListActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sjdj->{
                    intent.setClass(mContext, InspectionRegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //设备
    private fun initSb(sb: ArrayList<Threecd>?) {
        val list = ArrayList<ImageViewModel>()
        for (i in sb!!.indices) {
            when (sb[i].cdbh2) {
                "D010501" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_rcdj, "日常点检",sb[i].ftpdjmc))
                }
                "D010502" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_gzbx, "故障报修",sb[i].ftpdjmc))
                }
                "D010503" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_gzqr, "故障确认",sb[i].ftpdjmc))
                }
                "D010504" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sbwx, "设备维修",sb[i].ftpdjmc))
                }
                "D010505" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sbwb, "设备维保",sb[i].ftpdjmc))
                }
            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcSb.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image) {
                R.mipmap.work_sb_rcdj -> {
                    intent.setClass(mContext, EquipmentCheckActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sb_gzbx -> {
                    intent.setClass(mContext, EquipmentGuaranteeActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sb_gzqr -> {
                    intent.setClass(mContext, EquipmentFaultConfirmListActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sbwx -> {
                    intent.setClass(mContext, EquipmentRepairListActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sbwb -> {
                    intent.setClass(mContext, EquipmentMaintenanceScanListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //扫码出入库
    private fun initSmcrk(sm: ArrayList<Threecd>) {
        val smlist = ArrayList<ImageViewModel>()
        for (i in sm.indices) {
            when (sm[i].cdbh2) {
                "D010101" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_cgrk, "采购入库",sm[i].ftpdjmc))
                }
                "D010102" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_scwgrk, "生产完工入库",sm[i].ftpdjmc))
                }
                "D010103" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_llyk, "栈板转移",sm[i].ftpdjmc))
                }
                "D010104" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_llck, "领料出库",sm[i].ftpdjmc))
                }
                "D010105" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_xsfh, "销售发货出库",sm[i].ftpdjmc))
                }
                "D010106" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_ptyk, "普通移库",sm[i].ftpdjmc))
                }
                "D010107" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_kctz, "库存调整",sm[i].ftpdjmc))
                }
                "D010108" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_xstk, "销售退库",sm[i].ftpdjmc))
                }
                "D010109" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_cgtk, "采购退库",sm[i].ftpdjmc))
                }
            }
        }

        val crkadapter = ImageViewAdapter(mContext, smlist)
        v.rcCrk.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = crkadapter
        }
        crkadapter.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image) {
                R.mipmap.work_cgrk -> {
                    intent.setClass(mContext, InstorageActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_scwgrk -> {
                    intent.setClass(mContext, FinishingInActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_llyk -> {
                    intent.setClass(mContext, PickingMoveActivity::class.java)
                    intent.putExtra("type", "work")
                    startActivity(intent)
                }
                R.mipmap.work_llck -> {
                    intent.setClass(mContext, PickingOutboundActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_xsfh -> {
                    intent.setClass(mContext, SalesDeliveryOutActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_ptyk -> {
                    intent.setClass(mContext, OrdinaryMoveActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_kctz -> {
                    intent.setClass(mContext, InventoryAdjustmentActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_xstk -> {
                    intent.setClass(mContext, SalesRetreatActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_cgtk -> {
                    intent.setClass(mContext, ProcurementRetreatActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initMj(mj: ArrayList<Threecd>) {
        val list = ArrayList<ImageViewModel>()
        for (i in mj.indices) {
            when (mj[i].cdbh2) {
                "D010201" -> {
                    if (mj[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_mjsmck, "模具扫码出库",mj[i].ftpdjmc))
                }
                "D010202" -> {
                    if (mj[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_mjsmrk, "模具扫码入库",mj[i].ftpdjmc))
                }
            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcMj.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image){
                R.mipmap.work_mjsmck -> {
                    intent.setClass(mContext, MouldOutActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_mjsmrk -> {
                    intent.setClass(mContext, MouldInActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //工装管理
    private fun initGz(gz: ArrayList<Threecd>) {
        val list = ArrayList<ImageViewModel>()
        for (i in gz.indices) {
            when (gz[i].cdbh2) {
                "D010301" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzly, "工装领用",gz[i].ftpdjmc))
                }
                "D010302" -> {
                    if (gz[i].ifyqx2 == 1) {
                        list.add(ImageViewModel(R.mipmap.work_gzgh, "工装归还",gz[i].ftpdjmc))
                    }
                }
                "D010303" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzjc, "工装检查",gz[i].ftpdjmc))
                }
                "D010304" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_wxrk, "维修入库",gz[i].ftpdjmc))
                }
                "D010305" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzjj, "工装交接",gz[i].ftpdjmc))
                }
                "D010306" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzlylb, "工装领用列表",gz[i].ftpdjmc))
                }
                "D010307" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzsxx, "工装上下线",gz[i].ftpdjmc))
                }
                "D010308" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_djgh, gz[i].cdmc2,gz[i].ftpdjmc))
                }
            }

            val adapter = ImageViewAdapter(mContext, list)
            v.rcGz.let {
                it.layoutManager = GridLayoutManager(mContext, 4)
                it.adapter = adapter
            }
            adapter.onNewItemClick {image,url->
                val intent = Intent()
                intent.putExtra("conmap",url)
                when (image){
                    R.mipmap.work_gzly -> {
                        intent.setClass(mContext, ToolingRecipientsActivity::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_gzgh -> {
                        intent.setClass(mContext, ToolingBackActivity::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_gzjc -> {
                        intent.setClass(mContext, ToolingCheck::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_wxrk -> {
                        intent.setClass(mContext, ToolingMaintenance::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_gzjj -> {
                        intent.setClass(mContext, ToolingHandover::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_gzlylb -> {
                        intent.setClass(mContext, ToolingRecipientsList::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_gzsxx -> {
                        intent.setClass(mContext, ToolOnlineOfflineActivity::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.work_djgh -> {
                        intent.setClass(mContext, CutterReplace::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun initMy(my: ArrayList<Threecd>) {
        val list = ArrayList<ImageViewModel>()
        if (null == my) return
        for (i in my.indices) {
            when (my[i].cdbh2) {
                "D019901" -> {
                    if (my[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.my_unit, "我的加工\n单元",my[i].ftpdjmc))
                }
                "D019902" -> {
                    if (my[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.my_team, "我的生产\n小组",my[i].ftpdjmc))
                }
            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcMy.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {image,url->
            val intent = Intent()
            intent.putExtra("conmap",url)
            when (image) {
                R.mipmap.my_unit -> {
                    intent.setClass(mContext, ProcessingUnitActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.my_team -> {
                    intent.setClass(mContext, ProductionTeamActivity::class.java)
                    startActivity(intent)

                }
            }
        }
    }

    override fun initClick() {
        val intent = Intent()

        v.tvPlatform.clicks {
            intent.setClass(mContext, PlatformFreeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
    }


    override fun onResume() {
        super.onResume()
        vm.launchList({ vm.httpUtil.getPointCount(accout) },pointResult,isShowLoading = false, successCode = 200)
    }

}