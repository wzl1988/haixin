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
import com.eohi.hx.ui.work.quality.repair.RepairDisposeActivity
import com.eohi.hx.ui.work.quality.storage.StorageActivity
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
                        if (pointlist.name == "???????????????" && list[i].str == "???????????????") {
                            list[i].number = pointlist.count
                        }
                    }

                    for (index in zllist.indices){
                        if (pointlist.name == "????????????" && zllist[index].str == "????????????") {
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
                            intent.putExtra("type","start")
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
                        R.mipmap.work_sjdj->{
                            intent.setClass(mContext, InspectionRegisterActivity::class.java)
                            startActivity(intent)
                        }
                        R.mipmap.work_sbwx->{
                            intent.setClass(mContext, RepairDisposeActivity::class.java)
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

            //????????????
            if (zlglUsed) {
                initZlgl(zlgl)
            } else {
                v.llZlgl.gone()
            }

            //??????
            if (sbUsed) {
                initSb(sb)
            } else {
                v.llSb.visibility = View.GONE
            }
            //???????????????
            if (smUserd) {
                initSmcrk(sm!!)
            } else {
                v.llCrk.visibility = View.GONE
            }

            //?????????????????????
            if (mjUsed) {
                initMj(mj!!)
            } else {
                v.llMj.visibility = View.GONE
            }

            //????????????
            if (gzUsed) {
                initGz(gz!!)
            } else {
                v.llGz.visibility = View.GONE
            }

            //???????????????
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
            ToastUtil.showToast(mContext, "??????????????????")
        }

    }

    //???????????????
    private fun initZzp(zzp: ArrayList<Threecd>?) {

        Log.i("zlgl",zzp.toString())
        for (i in zzp!!.indices) {
            when (zzp[i].cdbh2) {
                "D010401" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gxwx, "????????????\n??????",zzp[i].ftpdjmc))
                }
                "D010402" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_wg, "????????????\n??????",zzp[i].ftpdjmc))
                }
                "D010403" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_clck, "????????????",zzp[i].ftpdjmc))
                }
                "D010404" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_kg, "??????",zzp[i].ftpdjmc))
                }
                "D010405" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_lzkxq, "???????????????",zzp[i].ftpdjmc))
                }
                "D010406" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_lzkck, "???????????????",zzp[i].ftpdjmc))
                }
                "D010407" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_scdj, "????????????",zzp[i].ftpdjmc))
                }
                "D010408" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_zl_bhgpdj, zzp[i].cdmc2,zzp[i].ftpdjmc))
                }
                "D010410" -> {
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_djylb, zzp[i].cdmc2,zzp[i].ftpdjmc))
                }
                "D010409" ->{
                    if (zzp[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sjdj, zzp[i].cdmc2,zzp[i].ftpdjmc))
                }
            }
        }

        list.add(ImageViewModel(R.mipmap.work_sbwx, "????????????",""))

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
                    intent.putExtra("type","start")
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
                R.mipmap.work_sjdj->{
                    intent.setClass(mContext, InspectionRegisterActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_sbwx->{
                    intent.setClass(mContext, RepairDisposeActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    //????????????
    private fun initZlgl(zlgl: ArrayList<Threecd>?) {

        for (i in zlgl!!.indices) {
            when (zlgl[i].cdbh2) {
                "D010601" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_lljy, "????????????",zlgl[i].ftpdjmc))
                }
                "D010602" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_sjjy, "????????????",zlgl[i].ftpdjmc))
                }
                "D010603" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_gcyj, "????????????",zlgl[i].ftpdjmc))
                }
                "D010604" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_wgjy, "????????????",zlgl[i].ftpdjmc))
                }
                "D010605" -> {
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_zl_fhjy, "????????????",zlgl[i].ftpdjmc))
                }
                "D010606" ->{
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_mjsmrk, "????????????",zlgl[i].ftpdjmc))
                }
                "D010607" ->{
                    if (zlgl[i].ifyqx2 == 1)
                        zllist.add(ImageViewModel(R.mipmap.work_kg, zlgl[i].cdmc2,zlgl[i].ftpdjmc))
                }
            }
        }


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
                R.mipmap.work_mjsmrk->{
                    intent.setClass(mContext, StorageActivity::class.java)
                    startActivity(intent)
                }
                R.mipmap.work_kg->{
                    intent.putExtra("type","cancel")
                    intent.setClass(mContext, StartWorkActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    //??????
    private fun initSb(sb: ArrayList<Threecd>?) {
        val list = ArrayList<ImageViewModel>()
        for (i in sb!!.indices) {
            when (sb[i].cdbh2) {
                "D010501" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_rcdj, "????????????",sb[i].ftpdjmc))
                }
                "D010502" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_gzbx, "????????????",sb[i].ftpdjmc))
                }
                "D010503" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sb_gzqr, "????????????",sb[i].ftpdjmc))
                }
                "D010504" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sbwx, "????????????",sb[i].ftpdjmc))
                }
                "D010505" -> {
                    if (sb[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_sbwb, "????????????",sb[i].ftpdjmc))
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

    //???????????????
    private fun initSmcrk(sm: ArrayList<Threecd>) {
        val smlist = ArrayList<ImageViewModel>()
        for (i in sm.indices) {
            when (sm[i].cdbh2) {
                "D010101" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_cgrk, "????????????",sm[i].ftpdjmc))
                }
                "D010102" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_scwgrk, "??????????????????",sm[i].ftpdjmc))
                }
                "D010103" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_llyk, "????????????",sm[i].ftpdjmc))
                }
                "D010104" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_llck, "????????????",sm[i].ftpdjmc))
                }
                "D010105" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_xsfh, "??????????????????",sm[i].ftpdjmc))
                }
                "D010106" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_ptyk, "????????????",sm[i].ftpdjmc))
                }
                "D010107" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_kctz, "????????????",sm[i].ftpdjmc))
                }
                "D010108" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_xstk, "????????????",sm[i].ftpdjmc))
                }
                "D010109" -> {
                    if (sm[i].ifyqx2 == 1)
                        smlist.add(ImageViewModel(R.mipmap.work_cgtk, "????????????",sm[i].ftpdjmc))
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
                        list.add(ImageViewModel(R.mipmap.work_mjsmck, "??????????????????",mj[i].ftpdjmc))
                }
                "D010202" -> {
                    if (mj[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_mjsmrk, "??????????????????",mj[i].ftpdjmc))
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

    //????????????
    private fun initGz(gz: ArrayList<Threecd>) {
        val list = ArrayList<ImageViewModel>()
        for (i in gz.indices) {
            when (gz[i].cdbh2) {
                "D010301" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzly, "????????????",gz[i].ftpdjmc))
                }
                "D010302" -> {
                    if (gz[i].ifyqx2 == 1) {
                        list.add(ImageViewModel(R.mipmap.work_gzgh, "????????????",gz[i].ftpdjmc))
                    }
                }
                "D010303" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzjc, "????????????",gz[i].ftpdjmc))
                }
                "D010304" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_wxrk, "????????????",gz[i].ftpdjmc))
                }
                "D010305" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzjj, "????????????",gz[i].ftpdjmc))
                }
                "D010306" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzlylb, "??????????????????",gz[i].ftpdjmc))
                }
                "D010307" -> {
                    if (gz[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_gzsxx, "???????????????",gz[i].ftpdjmc))
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
                        list.add(ImageViewModel(R.mipmap.my_unit, "????????????\n??????",my[i].ftpdjmc))
                }
                "D019902" -> {
                    if (my[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.my_team, "????????????\n??????",my[i].ftpdjmc))
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