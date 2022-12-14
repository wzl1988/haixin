package com.eohi.hx.ui.work.quality.delivery

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityDeliveryCheckBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.model.CommonPostModel
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showLongToast
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.Extensions.trimStr
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

/*
* ????????????
* */
class DeliveryCheckActivity : BaseActivity<DeliveryViewModel, ActivityDeliveryCheckBinding>(),
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
    private var wph = ""
    private var jydh = ""
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()

    companion object {
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        filterGdhList = ArrayList()
        gdhList = ArrayList()
        filterMygdhList = ArrayList()


        if (type == "modify") {
            v.tvTitle.text = "??????????????????"
            v.tvYjsl.gone()
            v.etJysl.gone()
            v.tvTmh.gone()
            v.etTmh.gone()
            v.tvJydh.show()
            v.etJydh.show()
            v.etHgsl.isEnabled = false
            v.btnGdh.isEnabled = false
            vm.getDetail(companyNo, jydh, djh)
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
                        Toast.makeText(this@DeliveryCheckActivity, "????????????????????????", Toast.LENGTH_SHORT)
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

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.imageAdd clicks {
            try {
                takeCameraPermissions()
            }catch (e:Exception){
                showLongToast(e.message.toString())
            }

        }
        v.tvTmh clicks {
            checkCameraPermissions()
        }
        v.btnGdh clicks {
            gdDialog = ListDialog(this, "????????????", filterGdhList, object : ListDialog.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvGdh.text = filterMygdhList[position]
                    vm.getDeliveryDetailGDH(companyNo, filterMygdhList[position])
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
        v.btnPost clicks {
            if (type == "modify") {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etHgsl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var tpwjm = ""
                        postPhoto.forEach {
                            tpwjm += "$it,"
                        }
                        model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.DJH = djh
                        model.JYDH = v.etJydh.text.toString()
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
                        model.JYSJ = v.tvRq.text.toString()
                        vm.modifyDeliveryCheck(model)
                    }
                }
            } else {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
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
                        model.JYSL =if(v.etJysl.text.toString().isEmpty()) 0.0 else v.etJysl.text.toString().toDouble()
                        model.JYMS = v.etJyms.text.toString()
                        model.TMH = v.tvTmh.text.toString()
                        model.JYYXM = username
                        model.DATA = list
                        model.BLYY = postBlxxList
                        model.JYYID = accout
                        model.JYSJ = v.tvRq.text.toString()
                        vm.postDeliveryCheck(model)
                    }
                }
            }
        }
        //??????1 ????????? 0
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
                MultiListDialog(this, "????????????", tempList, object : MultiListDialog.MyListener {
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
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
        postPhoto.clear()
        list = ArrayList()
        allGdhList = ArrayList()

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (intent.hasExtra("GDH")) {
            jydh = intent.getStringExtra("GDH")
        }
        if (intent.hasExtra("DJH")) {
            djh = intent.getStringExtra("DJH")
        }

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initVM() {
        vm.blxxList.observe(this) {
            blxxList.addAll(it)
        }
        vm.detail.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.etJydh.text = it.data.list[0].JYDH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvGdh.text = it.data.list[0].GDH

                if (it.data.list[0].JYJG == "1") {
                    v.pass.isChecked = true
                } else {
                    v.unPass.isChecked = true
                }

                if (it.data.BLYY != null && it.data.BLYY.size > 0) {
                    var str = ""
                    it.data.BLYY.forEachIndexed { i, bean ->
                        str += if (i == it.data.BLYY.size - 1) {
                            bean.xxsm
                        } else {
                            bean.xxsm + ","
                        }
                    }
                    v.tvBlxx.text = str
                    postBlxxList = it.data.BLYY
                }

                if (it.data.list[0].TPWJM != "") {
                    it.data.list[0].TPWJM.trim().split(",").forEach {
                        postPhoto.add(it)
                        mPicList.add("$apiurl/apidefine/image?filename=$it")
                    }
                }

                imgAdapter?.notifyDataSetChanged()

                total = if (type == "modify") {
                    (it.data.list[0].HGSL.toDouble() + it.data.list[0].BHGSL.toDouble()).toString()
                } else {
                    it.data.list[0].JYSL
                }

                v.etHgsl.setText(it.data.list[0].HGSL)
                v.etBhgsl.setText(it.data.list[0].BHGSL)

                v.etJyms.setText(it.data.list[0].JYMS)

                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ
                v.tvSapddh.text  = it.data.list[0].SAPDDH

            }
//            if (it.data.BT.size > 0) {
//                v.cardZjxm.show()
//                list.clear()
//                list.addAll(it.data.BT)
//                adapter.notifyDataSetChanged()
//            } else {
//                v.cardZjxm.gone()
//            }
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
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
        }
        vm.detailmodel.observe(this) {
            if (it.data.list.size > 0) {
                if (it.data.list[0].GDH != null) {
                    v.tvGdh.text = it.data.list[0].GDH
                }
                if(!it.data.list[0].DJH.isNullOrEmpty())
                djh = it.data.list[0].DJH

                wph = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvSapddh.text =  it.data.list[0].SAPDDH

                vm.getInspectionItems(wph, companyNo)
            }
//            if (it.data.BT.size > 0) {
//                v.cardZjxm.show()
//                list.clear()
//                list.addAll(it.data.BT)
//                adapter.notifyDataSetChanged()
//            } else {
//                v.cardZjxm.gone()
//            }
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

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, 1)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }

    //????????????
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
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                permissions.forEach {
                    if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, 100)
                        showShortToast("?????????????????????")
                        return
                    }
                }
                //HarmonyOS????????????
//                if (android.os.Build.BRAND == "Huawei" || android.os.Build.BRAND == "HUAWEI" ||
//                    Build.BRAND == "HONOR"
//                ) {
//                    val permissions = arrayOf(
//                        "ohos.permission.READ_USER_STORAGE",
//                        "ohos.permission.WRITE_USER_STORAGE",
//                        "ohos.permission.DISTRIBUTED_DATASYNC"
//                    )
//                    requestPermissions(permissions,0)
//                }
            }


            if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
                ToastUtil.showToast(
                    mContext,
                    "???????????????" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "??????"
                )
                return
            }
            //??????????????????
            ImageSelector.builder()
                .useCamera(true) // ????????????????????????
                .setSingle(false) //??????????????????
                .setViewImage(true) //??????????????????????????????,????????????true
                .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
                .start(this, IncomingCheckActivity.REQUEST_CODE) // ????????????

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_camera),
                IncomingCheckActivity.REQUEST_CODE,
                *perms
            )
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IncomingCheckActivity.REQUEST_CODE && data != null) {
            val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
            mPicList.addAll(images)
            imgAdapter?.notifyDataSetChanged()
            images?.forEach {
                val file = File(it)

                val requestBody: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var conmap= intent.getStringExtra("conmap")?:""
                if(conmap.isNullOrEmpty())
                    conmap ="fahuojianyan"
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
            //?????????????????????????????????
            val toDeletePicList =
                data!!.getStringArrayListExtra(PlusImageActivity.IMG_LIST) //???????????????????????????
            mPicList.clear()
            mPicList.addAll(toDeletePicList)
            imgAdapter?.notifyDataSetChanged()
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                v.tvTmh.text = data?.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trimStr()
                val tmh = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trim { it <= ' ' }
                vm.getDeliveryDetailTMH(
                    companyNo,
                    tmh
                )
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("??????", "????????????", "??????????????????????????????") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
            ToastUtil.showToast(
                mContext,
                "???????????????" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "??????"
            )
            return
        }
        //??????????????????
        ImageSelector.builder()
            .useCamera(true) // ????????????????????????
            .setSingle(false) //??????????????????
            .setViewImage(true) //??????????????????????????????,????????????true
            .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
            .start(this, IncomingCheckActivity.REQUEST_CODE) // ????????????
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
                v.tvTmh.text = result
                vm.getDeliveryDetailTMH(
                    companyNo,
                    result
                )
            }
        }
    }

    override fun onPause() {
        //??????????????????
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}