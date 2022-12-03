package com.eohi.hx.ui.work.quality.finish

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityFinishCheckBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.CommonPostModel
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity.Companion.RC_CAMERA
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity.Companion.REQUEST_CODE
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showLongToast
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.LogUtil
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.view.MultiListDialog
import com.eohi.hx.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.lang.Exception

/*
* 完工检验
* */
class FinishCheckActivity : BaseActivity<FinishViewModel, ActivityFinishCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()
    private lateinit var filterGdhList: ArrayList<String>
    private lateinit var allGdhList: ArrayList<String>
    private lateinit var gdhList: ArrayList<String>
    private lateinit var filterMygdhList: ArrayList<String>
    private lateinit var gdDialog: ListDialog
    private var model = CommonPostModel()
    private var total = "100"
    private var djh = ""
    private var jydh = ""
    private var wph = ""
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()

    companion object {
        var type = ""
        const val REQUEST_TMH = 0x001
        const val REQUEST_LZK = 0x002
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        if (type == "modify") {
            v.tvTitle.text = "完工检验修改"
            v.tvYjsl.gone()
            v.etJysl.gone()
            v.tvTmh.gone()
            v.btnGdh.isEnabled = false
            v.etHgsl.isEnabled = false
            v.tvGdh.isEnabled = false
            vm.getFinishCheckDetail(companyNo, jydh, djh)
        } else {
            getGDList()
        }

        vm.getBlxx()

        adapter = InspectionItemAdapter(this, list, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

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
                        Toast.makeText(this@FinishCheckActivity, "请先输入检验数量", Toast.LENGTH_SHORT)
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

    }

    private fun getGDList() {
        vm.getFinishCheckGDHList(companyNo)
    }

    private fun onTextResult(i: Int, s: String) {
        list[i].scz = s
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.ivBack clicks { finish() }
        v.imageAdd clicks {
            try {
                takeCameraPermissions()
            }catch (e:Exception){
                showLongToast(e.message.toString())
            }

        }
        v.tvTmh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.tvTmh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.tvTmh.width
                    - v.tvTmh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(REQUEST_TMH)
                }
                return false
            }
        })

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
                    checkCameraPermissions(REQUEST_LZK)
                }
                return false
            }
        })
        v.btnPost clicks {
            if (type == "modify") {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "工单号不能为空", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "请输入合格数量", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "请输入不合格数量", Toast.LENGTH_SHORT).show()
                    }
                    (v.etHgsl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "不合格数量不能大于合格数量", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var tpwjm = ""
                        postPhoto.forEach {
                            tpwjm += "$it,"
                        }
                        model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.DJH = djh
                        model.GSH = companyNo
                        model.WPH = wph
                        model.WPMC = v.tvWpmc.text.toString()
                        model.GG = v.tvGgms.text.toString()
                        model.JYJG = if (v.pass.isChecked) "1" else "2"
                        model.HGSL = if(v.etHgsl.text.toString().isEmpty()) 0.0 else v.etHgsl.text.toString().toDouble()
                        model.BHGSL =if(v.etBhgsl.text.toString().isEmpty()) 0.0 else v.etBhgsl.text.toString().toDouble()
                        model.GDH = v.tvGdh.text.toString()
                        model.JYSL = if(v.etJysl.text.toString().isEmpty()) 0.0 else v.etJysl.text.toString().toDouble()
                        model.JYMS = v.etJyms.text.toString()
                        model.TMH = v.tvTmh.text.toString()
                        model.DATA = list
                        model.BLYY = postBlxxList
                        model.SAPDDH = v.tvSapddh.text.toString()
                        model.JYYID = accout
                        model.LZKKH = v.tvLzkbh.text.toString()
                        model.JYSJ = v.tvRq.text.toString()
                        vm.modifyFinishCheck(model)
                    }
                }
            } else {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "工单号不能为空", Toast.LENGTH_SHORT).show()
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
                        model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.DJH = djh
                        model.GSH = companyNo
                        model.WPH = wph
                        model.WPMC = v.tvWpmc.text.toString()
                        model.GG = v.tvGgms.text.toString()
                        model.JYJG = if (v.pass.isChecked) "1" else "2"
                        model.HGSL = if(v.etHgsl.text.toString().isEmpty()) 0.0 else v.etHgsl.text.toString().toDouble()
                        model.BHGSL =if(v.etBhgsl.text.toString().isEmpty()) 0.0 else v.etBhgsl.text.toString().toDouble()
                        model.GDH = v.tvGdh.text.toString()
                        model.JYSL = if(v.etJysl.text.toString().isEmpty()) 0.0 else v.etJysl.text.toString().toDouble()
                        model.JYMS = v.etJyms.text.toString()
                        model.TMH = v.tvTmh.text.toString()
                        model.DATA = list
                        model.BLYY = postBlxxList
                        model.JYYID = accout
                        model.SAPDDH = v.tvSapddh.text.toString()
                        model.LZKKH = v.tvLzkbh.text.toString()
                        model.JYSJ = v.tvRq.text.toString()
                        model.JYYXM = username

                        vm.postFinishCheck(model)
                    }
                }
            }
        }
        v.btnGdh clicks {
            gdDialog = ListDialog(this, "选择工单", filterGdhList, object : ListDialog.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvGdh.text = filterMygdhList[position]
                    vm.getDetailGDH(companyNo, filterMygdhList[position])
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
                    showBlxx(false)
                }
                R.id.unPass -> {
                    showBlxx(true)
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

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (intent.hasExtra("GDH")) {
            jydh = intent.getStringExtra("GDH")
        }
        if (intent.hasExtra("DJH")) {
            djh = intent.getStringExtra("DJH")
        }

        list = ArrayList()
        filterGdhList = ArrayList()
        allGdhList = ArrayList()
        gdhList = ArrayList()
        filterMygdhList = ArrayList()

        postPhoto.clear()

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    override fun initVM() {
        vm.blxxList.observe(this) {
            blxxList.addAll(it)
        }
        vm.gdhList.observe(this) { it ->
            it.forEach {
                allGdhList.add(it.GDH+"/"+it.SCDDH)
                filterGdhList.add(it.GDH+"/"+it.SCDDH)
                gdhList.add(it.GDH)
                filterMygdhList.add(it.GDH)
            }
        }

        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
        }

        vm.response.observe(this) {
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show()
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
        }
        vm.detailmodel.observe(this) { it ->
            if (it.data.list.size > 0) {
//                if (type == "modify") {
//
//                    v.tvGdh.text = it.data.list[0].GDH
//                    v.tvSapddh.text = it.data.list[0].SAPDDH
//                    v.tvWpmc.text = it.data.list[0].WPMC
//                    v.tvGgms.text = it.data.list[0].GGMS
//                    v.tvWph.text = it.data.list[0].WPH
//                    total = if (type == "modify") {
//                        (it.data.list[0].HGSL.toDouble() + it.data.list[0].BHGSL.toDouble()).toString()
//                    } else {
//                        it.data.list[0].JYSL
//                    }
//
//                    if (it.data.BLYY != null && it.data.BLYY.size > 0) {
//                        var str = ""
//                        it.data.BLYY.forEachIndexed { i, bean ->
//                            str += if (i == it.data.BLYY.size - 1) {
//                                bean.xxsm
//                            } else {
//                                bean.xxsm + ","
//                            }
//                        }
//                        v.tvBlxx.text = str
//                        postBlxxList = it.data.BLYY
//                    }
//
//                    v.etHgsl.setText(it.data.list[0].HGSL)
//                    v.etBhgsl.setText(it.data.list[0].BHGSL)
//
//                    if (it.data.list[0].JYJG == "1") {
//                        v.pass.isChecked = true
//                    } else if (it.data.list[0].JYJG == "2") {
//                        v.unPass.isChecked = true
//                    }
//
//                    if (it.data.list[0].TPWJM != "") {
//                        it.data.list[0].TPWJM.trim().split(",").forEach {
//                            postPhoto.add(it)
//                            mPicList.add("$apiurl/apidefine/image?filename=$it")
//                        }
//                    }
//
//                    imgAdapter?.notifyDataSetChanged()
//
//                    v.etJyms.setText(it.data.list[0].JYMS)
//                    v.tvCzy.text = it.data.list[0].JYYXM
//                    v.tvRq.text = it.data.list[0].JYSJ
//                } else {
                    if(!it.data.list[0].DJH.isNullOrEmpty())
                    djh = it.data.list[0].DJH
                    wph = it.data.list[0].WPH
                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS
                    v.tvWph.text = wph
                    if(!it.data.list[0].GDH.isNullOrEmpty())
                    v.tvGdh.text = it.data.list[0].GDH
                    v.tvSapddh.text = it.data.list[0].SAPDDH
                    vm.getInspectionItems(wph, companyNo)
//                vm.getDetailGDH(companyNo,  v.tvGdh.text.toString())

//                }

//                if (it.data.BT.size > 0) {
//                    v.cardZjxm.show()
//                    list.clear()
//                    list.addAll(it.data.BT)
//                    adapter.notifyDataSetChanged()
//                } else {
//                    v.cardZjxm.gone()
//                }
            }else{
                v.tvWpmc.text = ""
                v.tvGgms.text =""
                v.tvWph.text = ""
                v.tvSapddh.text = ""
            }
        }

        vm.InspectionitemModelList.observe(this){
            if(it.isNotEmpty()){
                v.cardZjxm.show()
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }else{
                v.cardZjxm.gone()
            }
        }

    }

    @AfterPermissionGranted(RC_CAMERA)
    private fun checkCameraPermissions(requestCode:Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, requestCode)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), RC_CAMERA, *perms
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
//                if (android.os.Build.BRAND == "Huawei" || android.os.Build.BRAND == "HUAWEI" ||
//                    Build.BRAND == "HONOR"
//                ) {
//                    requestPermission()
//                }
            }

            if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
                ToastUtil.showToast(
                    mContext,
                    "最多能上传" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "张图"
                )
                return
            }
            //添加凭证图片
            ImageSelector.builder()
                .useCamera(true) // 设置是否使用拍照
                .setSingle(false) //设置是否单选
                .setViewImage(true) //是否点击放大图片查看,，默认为true
                .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // 图片的最大选择数量，小于等于0时，不限数量。
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


    //HarmonyOS获取权限
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        val permissions = arrayOf(
            "ohos.permission.READ_USER_STORAGE",
            "ohos.permission.WRITE_USER_STORAGE",
            "ohos.permission.DISTRIBUTED_DATASYNC"
        )
        requestPermissions(permissions,0)
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
                    conmap ="wangongjianyan"
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
        } else if (requestCode == REQUEST_TMH) {
            if (resultCode == RESULT_OK) {
                val result = data!!.getStringExtra(Constant.EXTRA_RESULT_CONTENT).toString().trim { it <= ' ' }
                v.tvTmh.setText(result)
                vm.getDetailTMH(
                    companyNo,
                    result
                )
            }
        }else if(requestCode == REQUEST_LZK){
            if (resultCode == RESULT_OK) {
                val result = data!!.getStringExtra(Constant.EXTRA_RESULT_CONTENT).toString().trim { it <= ' ' }
                v.tvLzkbh.setText(result)
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
        if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
            ToastUtil.showToast(
                mContext,
                "最多能上传" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "张图"
            )
            return
        }
        //添加凭证图片
        ImageSelector.builder()
            .useCamera(true) // 设置是否使用拍照
            .setSingle(false) //设置是否单选
            .setViewImage(true) //是否点击放大图片查看,，默认为true
            .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // 图片的最大选择数量，小于等于0时，不限数量。
            .start(this, IncomingCheckActivity.REQUEST_CODE) // 打开相册
    }

    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
        v.tvTmh
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvTmh.setText(result)
                when{
                    v.tvTmh.isFocused->
                    {
                        vm.getDetailTMH(
                            companyNo,
                            result
                        )
                    }
                    v.tvLzkbh.isFocused->
                        v.tvLzkbh.setText(result)
                }

            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}