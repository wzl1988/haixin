package com.eohi.hx.ui.work.quality.rejects

import android.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityRejectsDetermineBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.quality.rejects.model.Clbf
import com.eohi.hx.ui.work.quality.rejects.model.RejectsDetermineSubmitModel
import com.eohi.hx.ui.work.quality.rejects.model.SubmitRejectsItem
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

/**
 * 不良品判定
 */
class RejectsDetermineActivity : BaseActivity<RejectsDetermineViewModel,ActivityRejectsDetermineBinding>() {
    lateinit var listDatas: ArrayList<SubmitRejectsItem>
    lateinit var mAdapter: RejectsDetermineDisposalAdapter
    lateinit var wasteList :ArrayList<Clbf>
    lateinit var wasteAdapter:  RejectsDeterminewasteAdapter
    var blxx :ArrayList<String> = ArrayList()
    var blxxbm :ArrayList<String> = ArrayList()

    var blyy :ArrayList<String> = ArrayList()
    var blyybm :ArrayList<String> = ArrayList()

    private var gxhlist = ArrayList<String>()
    private var gxmclist = ArrayList<String>()

    private var scrlistshow = ArrayList<String>()
    private var scrlist = ArrayList<String>()
    private var scridList = ArrayList<String>()

    private var showitem = ArrayList<String>()
    private var wphlist = ArrayList<String>()
    private var wpmcList = ArrayList<String>()

    private var sbbh =""
    private var sbmc=""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.tvBlpdj.text = intent.getStringExtra("swh")
        v.tvLzk.text = intent.getStringExtra("lzkkh")
        v.tvWph.text = intent.getStringExtra("wph")
        v.tvCpmc.text = intent.getStringExtra("wpmc")
        v.tvCpth.text = intent.getStringExtra("th")
        v.tvScgx.text =  intent.getStringExtra("gx")
        v.tvBls.text = intent.getStringExtra("bls")
        v.tvScry.text = intent.getStringExtra("scry")
        sbbh = intent.getStringExtra("sbbh")?:""
        sbmc = intent.getStringExtra("sbmc")?:""
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.data
        v.tvScsb.text =sbmc+" "+sbbh

    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvScsb.clicks {
            vm.getSblist(companyNo, accout,intent.getStringExtra("jgdy"))
        }
        v.btnAdd.clicks {
            val model = SubmitRejectsItem(0,"","","","","",
                intent.getStringExtra("zzrxm"), intent.getStringExtra("zzrbm"),"",
                intent.getStringExtra("gxh"),intent.getStringExtra("gx"))
            listDatas.add(model)
            mAdapter.notifyItemInserted(listDatas.size-1)
        }
        v.btnAddFl.clicks {
            val model = Clbf(0,"","报废","")
            wasteList.add(model)
            wasteAdapter.notifyItemInserted(wasteList.size-1)
        }
        v.btnPost.clicks {
            var b= false
            for( i in listDatas.indices ){
                if(listDatas[i].blyybm.isNullOrEmpty()||listDatas[i].czfs.isNullOrEmpty()){
                    b=true
                    break
                }
            }
            if(b){
                showShortToast("处置方式和不良原因不能为空")
                return@clicks
            }

            var model = RejectsDetermineSubmitModel(
                v.tvBlpdj.text.toString(),
                v.tvWph.text.toString(),
                intent.getStringExtra("swrq"),
                v.tvBls.text.toString().toInt(), username, accout,DateUtil.audioTime,
                v.etBz.text.toString(),username, accout,wasteList,
                intent.getStringExtra("djr"),
                intent.getStringExtra("djrid"), companyNo,
                intent.getStringExtra("gxh"),
                intent.getStringExtra("gx"),0,listDatas,
                intent.getStringExtra("jgdybh"),
                intent.getStringExtra("jgdymc"),
                intent.getStringExtra("lzkkh"),
                intent.getStringExtra("rwdh"),
                sbbh,sbmc )
            vm.post(model)
        }

    }

    override fun initData() {
        listDatas = ArrayList()
        wasteList= ArrayList()
        val detail =intent.getParcelableArrayListExtra<SubmitRejectsItem>("detail")
        if(!detail.isNullOrEmpty())
        listDatas.addAll(detail)
        mAdapter = RejectsDetermineDisposalAdapter(this,listDatas)
        wasteAdapter = RejectsDeterminewasteAdapter(this,wasteList)
        v.rvMx.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter= mAdapter
        }
        v.rvFl.apply {
            layoutManager=LinearLayoutManager(mContext)
            adapter= wasteAdapter
        }
        mAdapter.setOnCzfs { i ->
            val list = arrayOf("报废","返修","让步接收","合格")
            showDialog(list,i)
        }
        mAdapter.setClickBlxx {
            showBlxx(blxx,it)
        }
        mAdapter.onDel {
            listDatas.removeAt(it)
            mAdapter.notifyDataSetChanged()
        }
        mAdapter.setClickBlyy {
            showBlyy(blyy,it)
        }

        mAdapter.setGxClick {
            showGx(it)
        }
        mAdapter.setPersonClick {
            showPerson(it)
        }

        mAdapter.setSl { i, s ->
            listDatas[i].blsl = s.toInt()
        }

        mAdapter.setSm { i, s ->
            listDatas[i].sm = s
        }

        wasteAdapter.setWP {
            showWpmc(it)
        }

        wasteAdapter.setClickBlxx {
            showWasteBlxx(it)
        }
        wasteAdapter.setSl { i, s ->
            wasteList[i].clbfsl= s.toInt()
        }
        wasteAdapter.onDel {
            wasteList.removeAt(it)
            wasteAdapter.notifyDataSetChanged()
        }

        vm.getBlxx()
        vm.getBlyy()
        vm.getGx(intent.getStringExtra("lzkkh"))
        vm.getPerson(companyNo,"")
        vm.getDisposalItemList(intent.getStringExtra("rwdh"))
    }

    override fun initVM() {
        vm.sblist.observe(this, Observer {
            if(it.isNotEmpty()){
                var list = ArrayList<String>()
                it.forEach {
                    list.add(it.sbmc+" "+it.sbbh)
                }
                val builder = AlertDialog.Builder(mContext, 3)
                builder.setItems(list.toTypedArray()) { dialog, which ->
                    dialog.dismiss()
                    sbbh = it[which].sbbh
                    sbmc = it[which].sbmc
                    v.tvScsb.text = it[which].sbmc
                }
                builder.create().show()
            }else{
                showShortToast("设备列表为空")
            }
        })
        vm.blxxList.observe(this, Observer { it ->
            it.forEach {bean->
                blxx.add(bean.xxsm)
                blxxbm.add(bean.xxbm)
            }

        })

        vm.blyyList.observe(this,Observer{
            if(it.isNotEmpty()){
                it.forEach {bean->
                    blyy.add(bean.yysm)
                    blyybm.add(bean.yybm)
                }
            }
        })

        vm.gxResult.observe(this, Observer {
            if(it.isNotEmpty()){
                it.onEach {model->
                    gxhlist.add(model.gxh)
                    gxmclist.add(model.gxms)
                }
            }
        })

        vm.personResult.observe(this, Observer {
            if(it.isNotEmpty()){
                it.forEach {model->
                    scrlistshow.add(model.ygxm+"("+model.ygbh+")")
                    scrlist.add(model.ygxm)
                    scridList.add(model.ygbh)
                }
            }
        })

        vm.itemlist.observe(this, Observer {
            if(it.isNotEmpty()){
                it.forEach {good->
                    showitem.add(good.wph+"("+good.gg+")")
                    wphlist.add(good.wph)
                    wpmcList.add(good.wpmc)
                }
            }
        })

        vm.result.observe(this,Observer{
            showShortToast("提交成功")
            App.post(EventMessage(EventCode.REFRESH, "", "", 0))
            finish()
        })
    }


    private fun showDialog( list: Array<String>,position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list) { dialog, which ->
            dialog.dismiss()
            listDatas[position].czfs = list[which]
            mAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }

    private fun showBlxx(list: ArrayList<String>, position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            listDatas[position].blxx = list[which]
            listDatas[position].blxxbm = blxxbm[which]
            mAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }

    private fun showBlyy(list: ArrayList<String>, position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            listDatas[position].blyy = list[which]
            listDatas[position].blyybm = blyybm[which]
            mAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }


    private fun showGx( position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(gxmclist.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            listDatas[position].zrgxh = gxhlist[which]
            listDatas[position].zrgxm = gxmclist[which]
            mAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }


    private fun showPerson( position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(scrlistshow.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            listDatas[position].scr = scrlist[which]
            listDatas[position].scrid = scridList[which]
            mAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }

    private fun showWpmc( position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(showitem.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            wasteList[position].clwph = wphlist[which]
            wasteAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }

    private fun showWasteBlxx( position:Int){
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(blxx.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            wasteList[position].clblxx = blxxbm[which]
            wasteAdapter.notifyDataSetChanged()
        }
        builder.create().show()
    }


}