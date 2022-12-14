package com.eohi.hx.ui.work.quality.process

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProcessCheckBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.ui.work.picking.outbound.PickingOutboundFragment
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity.Companion.MAX_SELECT_PIC_NUM
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity.Companion.REQUEST_CODE
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.Extensions.trimStr
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.view.MultiListDialog
import com.eohi.hx.view.MySpinnerAdapter
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import kotlinx.android.synthetic.main.activity_equipment_maintenance.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/*
* 过程检验
* */
class ProcessCheckActivity : BaseActivity<ProcessViewModel, ActivityProcessCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    private lateinit var cxList: ArrayList<String>
    private lateinit var cxidList: ArrayList<String>
    private lateinit var gxList: ArrayList<String>
    private lateinit var gxhList: ArrayList<String>
    private lateinit var filterGdhList: ArrayList<String>
    private lateinit var allGdhList: ArrayList<String>
    private lateinit var gdhList: ArrayList<String>
    private lateinit var filterMygdhList: ArrayList<String>
    private var gx = ""
    private var gxh = ""
    private var gdh = ""
    private var cxid = ""
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()
    private var jylx = ""
    private var jylxmc = ""
    private var jylxList = java.util.ArrayList<JylxBean>()
    private var jylxMcList = java.util.ArrayList<String>()
    private lateinit var jylxAdapter: ArrayAdapter<String>

    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()
    private var djh = ""

    private lateinit var cxDialog: ListDialog
    private lateinit var gdDialog: ListDialog

    private var model = CheckPostModel()
    private var total = "100"

    companion object {
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        adapter = InspectionItemAdapter(this, list, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        getCXList()
        getGDList()
        vm.getJylx("46")
        vm.getBlxx()
//        vm.getKgInfo( "RWD20221101007975002")

        v.spinnerJygx.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gx = gxList[position]
                gxh = gxhList[position]
                vm.getInspectionItems(v.tvWph.text.toString(),gxh, companyNo)
//                vm.getInspectionItems("100024824","3A009","01")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        v.etJysl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                v.etHgsl.text = s
            }

        })

        v.etBhgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (type == "modify") {
                    if (!TextUtils.isEmpty(v.etBhgsl.text.toString())) {
                        val num: String
                        val tmp = s.toString()
                        num = if (tmp.endsWith(".")) {
                            val a = tmp.substring(0, tmp.length - 1)
                            (total.toDouble() - a.toDouble()).toString()
                        } else {
                            (total.toDouble() - tmp.toDouble()).toString()
                        }
                        if (num.toDouble() >= 0) {
                            v.etHgsl.setText(num)
                        }
                    } else {
                        v.etHgsl.setText(total)
                    }
                } else {
                    if (TextUtils.isEmpty(v.etJysl.text)) {
                        Toast.makeText(this@ProcessCheckActivity, "请先输入检验数量", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!TextUtils.isEmpty(v.etBhgsl.text.toString())) {
                        val num =
                            (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                                .toDouble()).toString()
                        if (num.toDouble() >= 0) {
                            v.etHgsl.setText(num)
                        }
                    } else {
                        v.etHgsl.setText(v.etJysl.text.toString())
                    }
                }
            }

        })

        v.spJylx.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    return
                } else {
                    jylx = jylxList[position - 1].xmfldm
                    jylxmc = jylxList[position - 1].xmflmc
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

//        if (type == "modify") {
//            v.tvTitle.text = "过程检验修改"
//            v.tvGdh.isEnabled = false
//            v.etHgsl.isEnabled = false
//            v.etJyms.isEnabled = true
//            v.tvYjsl.gone()
//            v.etJysl.gone()
//            vm.getGxList(companyNo, gdh)
//            vm.getDetail(companyNo, gdh, gxh, djh)
//        }

    }

    private fun onTextResult(i: Int, s: String) {
        list[i].scz = s
    }

    private fun getCXList() {
        vm.getCXList(companyNo)
    }

    private fun getGDList() {
        vm.getGDList(companyNo)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.ivBack clicks { finish() }
        v.tvHistory.clicks {
            startActivity(Intent(this,ProcessListActivity::class.java))
        }
        v.tvLzkbh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.tvLzkbh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.tvLzkbh.width
                    - v.tvLzkbh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions()
                }
                return false
            }
        })
        v.btnPost clicks {
                when {
                    TextUtils.isEmpty(v.tvCx.text.toString()) -> {
                        Toast.makeText(this, "请先选择产线", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(v.tvGdh.text.toString()) -> {
                        Toast.makeText(this, "请选择工单号", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "请输入合格数量", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "请输入不合格数量", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etHgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "合格数量不能大于检验数量", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "不合格数量不能大于检验数量", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var tpwjm = ""
                        postPhoto.forEach {
                            tpwjm += "$it,"
                        }
                        model.tpwjm = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.lzkkh = v.tvLzkbh.text.toString().trimStr()
                        model.cxid = cxid
                        model.cx = v.tvCx.text.toString()
                        model.sapddh = v.tvSapddh.text.toString()
                        model.wph = v.tvWph.text.toString()
                        model.wpmc = v.tvWpmc.text.toString()
                        model.gg =  v.tvGgms.text.toString()
                        model.jyfs = ""
                        model.jyjg = if (v.pass.isChecked) "1" else "2"
                        model.jysl =  v.etJysl.text.toString().toDouble()
                        model.hgsl =  v.etHgsl.text.toString().toDouble()
                        model.bhgsl = if(v.etBhgsl.text.toString().isEmpty())0.0 else v.etBhgsl.text.toString().toDouble()
                        model.jylx = jylx
                        model.jylxmc = jylxmc
                        model.czr = username
                        model.czrq= DateUtil.data
                        model.gxh = gxh
                        model.gxmc = gx
                        model.czrid = accout
                        model.jyms = v.etJyms.text.toString()
                        model.DATA = list
                        vm.postResponse(model)
                    }
                }
        }
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.btnCx clicks {
            cxDialog = ListDialog(this, "选择产线", cxList, object : ListDialog.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvCx.text = cxList[position]
                    cxid = cxidList[position]
                }

            })
            cxDialog.show()
            cxDialog.hideSearch()
        }
        v.btnGdh clicks {
            gdDialog = ListDialog(this, "选择工单", filterGdhList, object : ListDialog.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvGdh.text = filterMygdhList[position]
                    vm.getGDResultList(companyNo, filterMygdhList[position])
                    vm.getGxList(companyNo, filterMygdhList[position])
                }
            })
            gdDialog.onSearchClick { s ->
                filterGdhList.clear()
                filterMygdhList.clear()
                if (TextUtils.isEmpty(s)) {
                    filterGdhList.addAll(allGdhList)
                } else {
                    for(i in allGdhList.indices){
                        if(allGdhList[i].contains(s)){
                            filterGdhList.add(allGdhList[i])
                            filterMygdhList.add(gdhList[i])
                        }
                    }
                }
                gdDialog.refreshList(filterGdhList)
            }
            gdDialog.show()
        }
        //合格1 不合格 0
        v.rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pass -> {
//                    showBlxx(false)
                }
                R.id.unPass -> {
//                    showBlxx(true)
                }
            }
        }
        v.tvBlxx clicks {
            val tempList = ArrayList<String>()
            blxxList.forEach {
                tempList.add(it.xxsm)
            }
            val dialog =
                MultiListDialog(this, "不良现象", tempList, object : MultiListDialog.MyListener {
                    override fun refreshActivity(listPosition: ArrayList<Int>) {
                        postBlxxList.clear()
                        var str = ""
                        listPosition.forEach {
                            str += blxxList[it].xxsm + ","
                            postBlxxList.add(blxxList[it])
                        }
                        v.tvBlxx.text = str.substring(0, str.length - 1)
                    }
                })
            dialog.show()
            dialog.hideSearch()
        }
    }

    private fun showBlxx(isShow: Boolean) {
        if (isShow) {
            v.llBlxx.show()
        } else {
            v.llBlxx.gone()
        }
    }

    override fun initData() {
        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime
//        if (intent.hasExtra("type")) {
//            type = intent.getStringExtra("type")
//        }
//        if (intent.hasExtra("GX")) {
//            gx = intent.getStringExtra("GX")
//        }
//        if (intent.hasExtra("GXH")) {
//            gxh = intent.getStringExtra("GXH")
//        }
//        if (intent.hasExtra("GDH")) {
//            gdh = intent.getStringExtra("GDH")
//        }
//        if (intent.hasExtra("DJH")) {
//            djh = intent.getStringExtra("DJH")
//        }

        list = ArrayList()
        cxList = ArrayList()
        gxList = ArrayList()
        cxidList = ArrayList()
        filterGdhList = ArrayList()
        allGdhList = ArrayList()
        gdhList = ArrayList()
        filterMygdhList = ArrayList()
        gxhList = ArrayList()

        postPhoto.clear()

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
        jylxAdapter = MySpinnerAdapter(this, android.R.layout.simple_spinner_item, jylxMcList)
        v.spJylx.adapter = jylxAdapter
    }

    override fun initVM() {
        vm.jylxBean.observe(this) {
            jylxList.addAll(it)
            jylxList.forEach { jylxBean ->
                jylxMcList.add(jylxBean.xmflmc)
                if(jylxmc.isNotEmpty()){
                    for (i in jylxMcList.indices){
                        if(jylxMcList[i]==jylxmc){
                            v.spJylx.setSelection(i+1,true)
                        }
                    }
                }
            }
        }
        vm.blxxList.observe(this) {
            blxxList.addAll(it)
        }
        vm.detailmodel.observe(this) {

        }
        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
        }
        vm.response.observe(this) {
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show()
                finish()
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
        }
        vm.gdResult.observe(this) {
//            it.data.list[0].apply {
//                v.tvWph.text = this.WPH
//                v.tvWpmc.text = this.WPMC
//                v.tvGgms.text = this.GGMS
//                v.tvSapddh.text = this.SAPDDH
//                djh = it.data.list[0].DJH
//            }
//            if (it.data.BT.size > 0) {
//                v.cardZjxm.show()
//                list.clear()
//                list.addAll(it.data.BT)
//                adapter.notifyDataSetChanged()
//            } else {
//                v.cardZjxm.gone()
//            }
        }
        vm.gxList.observe(this) { it ->
            if(it.isNullOrEmpty())
                return@observe
            gxList.clear()
            it.forEach {
                gxList.add(it.JYGX)
                gxhList.add(it.GXH)
            }
            val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, gxList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            v.spinnerJygx.adapter = adapter
            gxh =gxhList[0]
            gx =gxList[0]
            if (type == "modify") {
                val position = gxhList.indexOf(gxh)
                v.spinnerJygx.setSelection(position)
            }
        }
        vm.cxList.observe(this) { it ->
            it.forEach {
                cxList.add(it.cx)
                cxidList.add(it.cxid)
            }
        }
        vm.gdList.observe(this) { it ->
            it.forEach {
                allGdhList.add(it.GDH+"/"+it.SCDDH)
                filterGdhList.add(it.GDH+"/"+it.SCDDH)
                gdhList.add(it.GDH)
                filterMygdhList.add(it.GDH)
            }
        }


        vm.kgInfoList.observe(this){
            if(it.isNotEmpty()){
                v.tvCx.text = it[0].jgdymc
                v.tvGdh.text = it[0].rwdh
                v.tvSapddh.text = it[0].sapddh
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGgms.text = it[0].gg
                v.etJysl.setText(it[0].scsl.toString())
                vm.getGxList(companyNo,  it[0].rwdh)
            }
        }

        vm.InspectionitemModelList.observe(this){
            if(it.isNotEmpty()){
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_TITLE, "扫码")
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_IS_CONTINUOUS, com.eohi.hx.utils.Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                1,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }


    //查看大图
    private fun viewPluImg(position: Int) {
        val intent =
            Intent(this, PlusImageActivity::class.java)
        intent.putStringArrayListExtra(
            PlusImageActivity.IMG_LIST,
            mPicList
        )
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

    private fun takeCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                permissions.forEach {
                    if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, 100)
                        showShortToast("权限获取失败！")
                        return
                    }
                }
                //HarmonyOS获取权限
                if (android.os.Build.BRAND == "Huawei" || android.os.Build.BRAND == "HUAWEI" ||
                    Build.BRAND == "HONOR"
                ) {
                    val permissions = arrayOf(
                        "ohos.permission.READ_USER_STORAGE",
                        "ohos.permission.WRITE_USER_STORAGE",
                        "ohos.permission.DISTRIBUTED_DATASYNC"
                    )
                    requestPermissions(permissions,0)
                }
            }

            if (mPicList.size >= MAX_SELECT_PIC_NUM) {
                ToastUtil.showToast(
                    mContext,
                    "最多能上传" + MAX_SELECT_PIC_NUM + "张图"
                )
                return
            }
            //添加凭证图片
            ImageSelector.builder()
                .useCamera(true) // 设置是否使用拍照
                .setSingle(false) //设置是否单选
                .setViewImage(true) //是否点击放大图片查看,，默认为true
                .setMaxSelectCount(MAX_SELECT_PIC_NUM - mPicList.size) // 图片的最大选择数量，小于等于0时，不限数量。
                .start(this, REQUEST_CODE) // 打开相册

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_camera),
                REQUEST_CODE,
                *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
            mPicList.addAll(images)
            imgAdapter?.notifyDataSetChanged()
            images?.forEach {
                val file = File(it)

                val requestBody: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var conmap= intent.getStringExtra("conmap")?:""
                if(conmap.isNullOrEmpty())
                    conmap ="guochengxunjian"
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "filename",
                        file.name,
                        requestBody
                    )
                    .addFormDataPart("busclass", "1")
                    .addFormDataPart("acesstype", "1")
                    .addFormDataPart("conmap", conmap)
                    .addFormDataPart("workorderno", djh)
                    .build()

                vm.getResult(body)
            }
        } else if (requestCode == PlusImageActivity.REQUEST_CODE_MAIN && resultCode == PlusImageActivity.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            val toDeletePicList =
                data!!.getStringArrayListExtra(PlusImageActivity.IMG_LIST) //要删除的图片的集合
            mPicList.clear()
            mPicList.addAll(toDeletePicList)
            imgAdapter?.notifyDataSetChanged()
        }else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                v.tvLzkbh.setText(data?.getStringExtra(Intents.Scan.RESULT)!!.trimStr())
                val lzkh = data.getStringExtra(Intents.Scan.RESULT)!!.trim { it <= ' ' }
                vm.getKgInfo(lzkh)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("确定", "重要提示", "请去设置里开启权限！") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (mPicList.size >= MAX_SELECT_PIC_NUM) {
            ToastUtil.showToast(
                mContext,
                "最多能上传" + MAX_SELECT_PIC_NUM + "张图"
            )
            return
        }
        //添加凭证图片
        ImageSelector.builder()
            .useCamera(true) // 设置是否使用拍照
            .setSingle(false) //设置是否单选
            .setViewImage(true) //是否点击放大图片查看,，默认为true
            .setMaxSelectCount(MAX_SELECT_PIC_NUM - mPicList.size) // 图片的最大选择数量，小于等于0时，不限数量。
            .start(this, REQUEST_CODE) // 打开相册
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
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trimStr()
                v.tvLzkbh.setText(result)
                vm.getKgInfo(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }


}