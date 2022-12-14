package com.eohi.hx.ui.work.quality.register

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityInspectionRegisterBinding
import com.eohi.hx.ui.work.model.ItemModel
import com.eohi.hx.ui.work.model.RegisterSubmitModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.view.MultiListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class InspectionRegisterActivity : BaseActivity<InspectionRegisterViewModel, ActivityInspectionRegisterBinding>() {
    private lateinit var dialogPerson: ListDialog
    private var listPerson = ArrayList<String>()
    private lateinit var dialogItem:ListDialog
    private var listItem = ArrayList<String>()
    var itemlist:ArrayList<ItemModel>?=null
    private lateinit var diaologGx: MultiListDialog
    private var listGX= ArrayList<String>()
    private var gxh:String =""
    private var gxmc:String =""
    private var sjr = username
    private var sjrid= accout
    private var wph=""
    private var wpmc =""
    override fun isNeedEventBus(): Boolean {
       return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
//        vm.getKgInfo("210902000001")

    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.sjrSpinner.clicks {
            if(listPerson.isEmpty()){
                vm.findJYRY(companyNo, "")
            }else{
                dialogPerson.show()
            }
        }
        v.wpSpinner.clicks {
//            if(listItem.isEmpty()){
//                vm.getItemInfo(wph)
//            }else{
//                dialogItem.show()
//                dialogItem.hideSearch()
//            }

        }

        v.gxSpinner.clicks {
            if(listGX.isEmpty()){
                vm.getGXList(wph)
            }else{
                diaologGx.show()
                diaologGx.hideSearch()
            }

        }

        v.tvLzkbh.clicks {
            checkCameraPermissions(1)
        }

        v.btnPost.clicks {
            if(v.wpSpinner.text.isEmpty()){
                showShortToast("??????????????????")
                return@clicks
            }
            if(v.gxSpinner.text.isEmpty()){
                showShortToast("??????????????????")
                return@clicks
            }

            if(v.etJysl.text.isEmpty()){
                showShortToast("????????????????????????")
                return@clicks
            }

            vm.submit(
                RegisterSubmitModel(username, accout,  v.tvRq.text.toString(),
            v.tvGg.text.toString(),gxh?:"",gxmc?:"",v.tvSjsj.text.toString(),v.etJysl.text.toString()
            ,v.spinner.selectedItem.toString(),sjr,sjrid,wph,wpmc,v.tvRwdh.text.toString(),v.tvScddh.text.toString(),v.tvLzkbh.text.toString())
            )
        }

    }

    override fun initData() {
        v.sjrSpinner.text = "$accout $username"
        v.tvSjsj.text = DateUtil.dataTime
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.dataTime
        initSpinner()
        dialogPerson = ListDialog(this, "????????????", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.sjrSpinner.text  = listPerson[position]
                val res=listPerson[position].split(" ")
                sjrid = res[0]
                sjr = res[1]
            }
        })
        dialogPerson.onSearchClick {
            vm.findJYRY(companyNo,it)
        }

        dialogItem =  ListDialog(this, "????????????", listItem, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.wpSpinner.text = listItem[position]
                itemlist?.let {
                    wph = it[position].wph
                    wpmc = it[position].wpmc
                    v.tvGg.text = it[position].gg
                    listGX.clear()
                }
            }
        })
        diaologGx =  MultiListDialog(this, "????????????", listGX, object : MultiListDialog.MyListener {

            override fun refreshActivity(listPosition: ArrayList<Int>) {
//                v.gxSpinner.text = listGX[position]
//                val res=listGX[position].split(" ")
//                gxh = res[0]
//                gxmc =res[1]
                var str =StringBuffer()
                val gxhstr = StringBuffer()
                listPosition.forEachIndexed { index, i ->
                    val res=listGX[i].split(" ")
                    val mgxh = res[0]
                    val mgxmc =res[1]
                    str.append(mgxmc)
                    gxhstr.append(mgxh)
                    if(index !=  (listPosition.size-1)){
                        str.append(",")
                        gxhstr.append(",")
                    }
                    gxmc = str.toString()
                    gxh = gxhstr.toString()
                }

                v.gxSpinner.text = str.toString()
            }
        })


    }

    override fun initVM() {
        vm.kgInfoList.observe(this, Observer {
            if(it.isNotEmpty()){
                v.tvRwdh.text = it[0].rwdh
                v.wpSpinner.text = it[0].wph+" "+it[0].wpmc
                v.tvGg.text = it[0].gg
//                v.gxSpinner.text = it[0].gxh+" "+it[0].gxmc
                v.tvScddh.text = it[0].sapddh
                wph= it[0].wph
                wpmc = it[0].wpmc
                listItem.clear()
                listGX.clear()
//                gxh = it[0].gxh
//                gxmc = it[0].gxmc
//                vm.getGXList(it[0].wph)
//                vm.getItemInfo(it[0].wph)
            }
        })

        vm.person.observe(this, Observer { it ->
            if (it.isNotEmpty()) {
                listPerson.clear()
                it.forEach { model ->
                    listPerson.add(model.ygbh + " " + model.ygxm)
                }
                dialogPerson.apply {
                    show()
                    refreshList(listPerson)
                }
            } else {
                showShortToast("???????????????")
            }
        })

        vm.itemlist.observe(this, Observer {
            if(it.isNotEmpty()){
                if(listItem.isEmpty()){
                    itemlist = it
                    it.forEach {model->
                        listItem.add(model.wph+" "+model.wpmc)
                    }
                }
                dialogItem.run {
                    show()
                    refreshList(listItem)
                    hideSearch()
                }
            }
        })

        vm.gxlist.observe(this, Observer {
            if(it.isNotEmpty()){
                if(listGX.isEmpty()){
                    it.forEach {model->
                        listGX.add(model.gxh+" "+model.gxmc)
                    }
                }
                diaologGx.apply {
                    show()
                    hideSearch()
                }

            }
        })

        vm.result.observe(this, Observer {
            showShortToast("???????????????")
            v.tvRwdh.text =""
            v.wpSpinner.text = ""
            v.tvGg.text =""
            v.tvLzkbh.setText("")
            v.sjrSpinner.text=""
            v.gxSpinner.text =""
            v.etJysl.setText("")
        })
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // ?????????????????????
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvLzkbh.setText(result)
                vm.getKgInfo(result)
            }
        }
    }

    override fun onPause() {
        //??????????????????
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    private fun initSpinner() {
        val splist = java.util.ArrayList<String>()
        splist.add("????????????")
        splist.add("????????????")
        val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, splist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        v.spinner.adapter = adapter
        v.spinner.setSelection(0)
        val onitemlistener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        v.spinner.onItemSelectedListener = onitemlistener
    }
    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_TITLE, "??????")
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_IS_CONTINUOUS, com.eohi.hx.utils.Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                code,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                1 -> {
                   val str= data.getStringExtra(Intents.Scan.RESULT)!!.trim { it <= ' ' }
                    v.tvLzkbh.setText(str)
                    vm.getKgInfo(str)
                }
            }
        }
    }


}