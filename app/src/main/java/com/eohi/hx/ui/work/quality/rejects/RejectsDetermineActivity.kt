package com.eohi.hx.ui.work.quality.rejects

import android.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityRejectsDetermineBinding
import com.eohi.hx.ui.work.quality.rejects.model.SubmitRejectsItem
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

/**
 * 不良品判定
 */
class RejectsDetermineActivity : BaseActivity<RejectsDetermineViewModel,ActivityRejectsDetermineBinding>() {
    lateinit var listDatas: ArrayList<SubmitRejectsItem>
    lateinit var mAdapter: RejectsDetermineDisposalAdapter
    var blxx :ArrayList<String> = ArrayList()
    var blxxbm :ArrayList<String> = ArrayList()

    var blyy :ArrayList<String> = ArrayList()
    var blyybm :ArrayList<String> = ArrayList()
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
    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvScsb.clicks {
            vm.getSblist(companyNo, accout,intent.getStringExtra("jgdy"))
        }
        v.btnAdd.clicks {
            val model = SubmitRejectsItem(0,"","","","","",
                intent.getStringExtra("zzrxm"), "","",
                intent.getStringExtra("gxh"),intent.getStringExtra("gx"))
            listDatas.add(model)
            mAdapter.notifyItemInserted(listDatas.size-1)
        }

    }

    override fun initData() {
        listDatas = ArrayList()
        mAdapter = RejectsDetermineDisposalAdapter(this,listDatas)
        v.rvMx.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter= mAdapter
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

        vm.getBlxx()
        vm.getBlyy()
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
                    v.tvScsb.text = it[which].sbmc
                }
                builder.create().show()
            }
        })
        vm.blxxList.observe(this, Observer { it ->
            it.forEach {bean->
                blxx.add(bean.XXSM)
                blxxbm.add(bean.XXBM)
            }

        })

        vm.blyyList.observe(this,Observer{
            if(it.isNotEmpty()){
                it.forEach {bean->
                    blyy.add(bean.YYSM)
                    blyybm.add(bean.YYBM)
                }
            }
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


}