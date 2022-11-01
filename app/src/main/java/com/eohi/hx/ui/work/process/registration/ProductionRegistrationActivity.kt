package com.eohi.hx.ui.work.process.registration

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProductionRegistrationBinding
import com.eohi.hx.ui.work.adapter.AddConsumablesAdapter
import com.eohi.hx.ui.work.model.ClModel
import com.eohi.hx.ui.work.model.MaterialModel
import com.eohi.hx.ui.work.model.ProductionSubList
import com.eohi.hx.ui.work.model.ProductionSubmitModel
import com.eohi.hx.ui.work.process.viewmodel.ProductionRegistrationViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogAddConsumables
import com.eohi.hx.view.DialogEqu
import com.eohi.hx.view.DialogSelectList
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ProductionRegistrationActivity :
    BaseActivity<ProductionRegistrationViewModel, ActivityProductionRegistrationBinding>(),
    DialogAddConsumables.SetData, DialogAddConsumables.onBtnClick {
    private var gxno = ""
    private var equNO = ""
    private var jgdybh = ""
    private var scbz = ""
    private var zrrBh = accout
    private var produceuserid = accout
    private var producetype = "个人"
    private var isfinish = "否"
    private var glgxlist = ArrayList<String>()
    private lateinit var listEquipment: ArrayList<String>
    private lateinit var listSbbh: ArrayList<String>
    private lateinit var dialogEquipment: DialogEqu
    private lateinit var dialogJgdy: ListDialog
    private lateinit var listJgdy: ArrayList<String>
    private lateinit var listJgdybh: ArrayList<String>
    private lateinit var listPerson: ArrayList<String>
    private lateinit var listPersonBh: ArrayList<String>
    private lateinit var dialogPerson: ListDialog
    private lateinit var zzrlist: ArrayList<String>
    private lateinit var zzrbhlist: ArrayList<String>
    private lateinit var dialogzrr: ListDialog
    private lateinit var hashMap: HashMap<String, String>
    private var dialog: DialogAddConsumables? = null
    private lateinit var mMateriallist: ArrayList<MaterialModel>
    private lateinit var adpater: AddConsumablesAdapter
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        mMateriallist = ArrayList()
        adpater = AddConsumablesAdapter(this, mMateriallist)
        v.rcCl.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater.onItemClick {
            mMateriallist.removeAt(it)
            adpater.notifyDataSetChanged()
        }

        v.etBgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (v.tvXqsl.text.isNotEmpty() &&
                    s.toString().isNotEmpty()
                    && v.tvYbgs.text.isNotEmpty()
                ) {
                    val xqs = v.tvXqsl.text.toString().toFloat()
                    val bgs = s.toString().toFloat()
                    val ybsl = v.tvYbgs.text.toString().toFloat()
                    if ((bgs + ybsl) >= xqs) {
                        v.pass.isChecked = true
                    }
                }
            }
        })

    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.ivScan.clicks {
            checkCameraPermissions()
        }
        v.btnScgx.clicks {
            vm.getProductionProcesses(v.etLzkkh.text.toString(), accout)
        }
        v.btnScsb.clicks {
            dialogEquipment = DialogEqu(this, listEquipment, object : DialogEqu.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvScsb.text = listEquipment[position]
                    equNO = listSbbh[position]
                    v.tvJgdy.text = listJgdy[position]
                    jgdybh = listJgdybh[position] ?: ""
                }
            })
            dialogEquipment.setOnEqu {
                vm.getSblist(companyNo, accout)
            }
            dialogEquipment.setOnAllequ {
                getAll()
            }
            vm.getSblist(companyNo, accout)
        }
        v.btnJgdy.clicks {
            vm.getJgdy(v.etLzkkh.text.toString(), gxno, equNO)
        }

        v.btnScry.clicks {
            vm.getPerson(companyNo, "")
        }

        v.btnAdd.clicks {
            dialog = DialogAddConsumables(mContext, activity = mContext)
            dialog?.sedata = this
            dialog?.onbtnClick = this
            dialog?.show()
        }
        v.btnScbz.clicks {
            vm.getProductTeam(username)
        }
        v.btnZrr.clicks {
            vm.getZzrlist(companyNo, "")
        }

        v.btnSubmit.clicks {
            if (v.etLzkkh.text.isEmpty()) {
                showShortToast("流转卡号不能为空")
                return@clicks
            }
            if (v.etScgx.text.isEmpty()) {
                showShortToast("生产工序不能为空")
                return@clicks
            }
            if (v.etBgsl.text.isEmpty()) {
                showShortToast("报工数不能为空")
                return@clicks
            }

            var model = ProductionSubmitModel()
            var list = ArrayList<ProductionSubmitModel>()
            model.cardno = v.etLzkkh.text.toString()
            model.boxno = v.etZzxh.text.toString()
            model.equno = equNO
            model.gxno = gxno
            model.produceuserid = produceuserid
            model.producetype = producetype
            model.zrr = zrrBh
            model.scbz = scbz
            model.sl = v.etBgsl.text.toString().toInt()
            model.jgdybh = jgdybh
            model.userid = accout
            model.wlbs = ""
            model.isfinish = isfinish
            list.add(model)
            glgxlist.onEach {
                model.gxno = it
                list.add(model)
            }
            var cllist = ArrayList<ClModel>()
            mMateriallist.forEach {
                var clmodel = ClModel(
                    v.etLzkkh.text.toString(), it.clbzh, it.wph, gxno
                )
                cllist.add(clmodel)
            }
            vm.submit(ProductionSubList(list, cllist))
        }


    }

    override fun initData() {
        v.tvCzr.text = username
        v.tvRq.text = DateUtil.data
        v.tvScry.text = username
        v.tvZrr.text = username
        hashMap = HashMap<String, String>()
        listJgdy = ArrayList()
        listJgdybh = ArrayList()
        listSbbh = ArrayList()
        listEquipment = ArrayList()
        listPerson = ArrayList()
        listPersonBh = ArrayList()
        zzrlist = ArrayList()
        zzrbhlist = ArrayList()
        dialogJgdy = ListDialog(this, "加工单元", listJgdy, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvJgdy.text = listJgdy[position]
                jgdybh = listJgdybh[position]
            }
        })




        dialogPerson = ListDialog(this, "人员选择", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScry.text = listPerson[position]
                produceuserid = listPersonBh[position]
            }
        })
        dialogPerson.onSearchClick {
            vm.getPerson(companyNo, it)
        }

        dialogzrr = ListDialog(this, "选择责任人", zzrlist, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvZrr.text = zzrlist[position]
                zrrBh = zzrbhlist[position]
            }
        })
        dialogzrr.onSearchClick {
            vm.getZzrlist(companyNo, it)
        }

        initRg()

    }

    private fun initRg() {
        v.tvScbz.visibility = View.GONE
        v.tvScbzText.visibility = View.GONE
        v.btnScbz.visibility = View.GONE
        v.tvZrr.visibility = View.GONE
        v.tvZrrText.visibility = View.GONE
        v.btnZrr.visibility = View.GONE
        v.rgFs.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_gr -> {
                    v.tvScbz.visibility = View.GONE
                    v.tvScbzText.visibility = View.GONE
                    v.btnScbz.visibility = View.GONE
                    v.tvZrr.visibility = View.GONE
                    v.tvZrrText.visibility = View.GONE
                    v.btnZrr.visibility = View.GONE
                    zrrBh = ""
                    scbz = ""
                    producetype = "个人"
                }
                R.id.rb_jt -> {
                    v.tvScbz.visibility = View.VISIBLE
                    v.tvScbzText.visibility = View.VISIBLE
                    v.btnScbz.visibility = View.VISIBLE
                    v.tvZrr.visibility = View.VISIBLE
                    v.tvZrrText.visibility = View.VISIBLE
                    v.btnZrr.visibility = View.VISIBLE
                    zrrBh = accout
                    producetype = "集体"
                }
            }
        }

        v.rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.pass -> isfinish = "是"
                R.id.unPass -> isfinish = "否"
            }

        }


    }

    override fun initVM() {
        vm.cardinfo.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvCpmc.text = it[0].wpmc
                v.tvTh.text = it[0].th
                v.tvScsl.text = it[0].scsl.toString()
                v.tvXqsl.text = it[0].bzs.toString()
            }
        })

        vm.process.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                val strlist = ArrayList<String>()
                list.forEach {
                    strlist.add(it.GXMS)
                }

                val dialog = DialogSelectList(mContext, mContext, "选择工序", strlist)
                dialog.onItemClick {
                    v.etScgx.text = strlist[it]
                    v.tvYbgs.text = list[it].DQBZNSL.toString()
                    gxno = list[it].GXH
                    vm.getGlgx(v.etLzkkh.text.toString(), gxno)
                }
                dialog.show()
            }
        })

        vm.sblist.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                listEquipment.clear()
                listSbbh.clear()
                listJgdy.clear()
                listJgdybh.clear()
                list.onEach {
                    listSbbh.add(it.sbbh)
                    listEquipment.add(it.sbmc)
                    listJgdy.add(it.scxmc)
                    listJgdybh.add(it.scxbh)
                }
                dialogEquipment.show()
                dialogEquipment.refreshList(listEquipment)

            }
        })

        vm.jgdylist.observe(this, Observer {
            listJgdy.clear()
            listJgdybh.clear()
            it.onEach {
                listJgdybh.add(it.jgdybh)
                listJgdy.add(it.jgdymc)
            }
            dialogJgdy.show()
            dialogJgdy.refreshList(listJgdy)
        })

        vm.personlist.observe(this, Observer {
            listPerson.clear()
            listPersonBh.clear()
            it.onEach {
                listPerson.add(it.ygxm)
                listPersonBh.add(it.ygbh)
            }
            dialogPerson.show()
            dialogPerson.refreshList(listPerson)
        })

        vm.material.observe(this, Observer {
            if (it.isNotEmpty()) {
                dialog?.update(it[0])
            }
        })

        vm.glgxlist.observe(this, Observer {
            if (it.isNotEmpty()) {
                glgxlist.clear()
                var strbuff = StringBuffer()
                for (i in it.indices) {
                    glgxlist.add(it[i].gxh)
                    strbuff.append(it[i].gxms)
                    if (i < it.size - 1)
                        strbuff.append(",")
                }
                v.tvGlgx.text = strbuff
            }
        })

        vm.teams.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                var strlist = ArrayList<String>()
                list.forEach {
                    strlist.add(it.xzmc)
                }
                val dialog = DialogSelectList(mContext, mContext, "选择班组", strlist)
                dialog.onItemClick {
                    scbz = list[it].XZBH
                    v.tvScbz.text = strlist[it]
                }
                dialog.show()
            }
        })

        vm.zzrlist.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                zzrlist.clear()
                zzrbhlist.clear()
                list.forEach {
                    zzrlist.add(it.ygxm)
                    zzrbhlist.add(it.ygbh)
                }
                dialogzrr.show()
                dialogzrr.refreshList(zzrlist)
            }

        })
        vm.equipmentList.observe(this, Observer {
            if (it.isNotEmpty()) {
                listEquipment.clear()
                listSbbh.clear()
                listJgdy.clear()
                listJgdybh.clear()
                it.onEach {
                    listSbbh.add(it.sbbh)
                    listEquipment.add(it.sbmc)
                    listJgdy.add(it.scxmc)
                    listJgdybh.add(it.scxbh)
                }
                dialogEquipment.refreshList(listEquipment)
            }
        })

        vm.result.observe(this, Observer {
            finish()
            showShortToast("提交成功")
        })

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
                Constant.REQUEST_CODE_SCAN_02,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
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
            if (intent.action == SCANACTION && !(dialog?.isShowing ?: false)) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etLzkkh.setText(result)
                vm.getCardInfo(result, accout)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    private fun initMap() {
        hashMap["companycode"] = companyNo
        hashMap["equname"] = ""
        hashMap["userid"] = accout
    }

    private fun getAll() {
        initMap()
        vm.getAllEquipmentList(hashMap)
    }

    override fun getData(str: String) {
        vm.getMaterial(str, accout)
    }

    override fun onBtnClick(model: MaterialModel) {
        dialog?.dismiss()
        var isexit = true
        for (i in mMateriallist.indices) {
            if (model.clbzh == mMateriallist[i].clbzh) {
                isexit = false
                break
            }
        }
        if (isexit) {
            mMateriallist.add(model)
            adpater.notifyDataSetChanged()
        } else {
            showShortToast("物料已存在")
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    dialog?.update(result)
                }
                Constant.REQUEST_CODE_SCAN_02 -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.etLzkkh.setText(result)
                    vm.getCardInfo(result, accout)
                }
            }
        }
    }


}