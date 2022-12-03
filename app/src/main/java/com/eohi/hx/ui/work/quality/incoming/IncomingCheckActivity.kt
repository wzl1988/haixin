package com.eohi.hx.ui.work.quality.incoming

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityIncomingCheckBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.model.CheckPostModel
import com.eohi.hx.ui.work.model.CommonPostModel
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.activity_equipment_maintenance.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/*
* 来料检验
* */
class IncomingCheckActivity : BaseActivity<IncomingViewModel, ActivityIncomingCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    var imgAdapter: ImageAdapter? = null
    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    private lateinit var hashMap: HashMap<String, String>
    private var model = CommonPostModel()
    private var rwbh = ""
    private var djh = ""
    private var cgddh = ""
    private var total = "100"
    var mPicList = ArrayList<String>()

    companion object {
        const val MAX_SELECT_PIC_NUM = 3
        const val REQUEST_CODE = 0x00000011
        const val RC_CAMERA = 0X01
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

        v.etBhgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                v.etJssl.setText(v.etBhgsl.text.toString())

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
                        v.etHgsl.text = num
                    }
                } else {
                    v.etHgsl.text = total
                }
            }

        })

        initMap()
        if(type=="modify"){
            v.tvTitle.text = "来料检验修改"
                vm.getDetail(hashMap)
        }
//
//        when (type) {
//            "modify" -> {
//                v.tvTitle.text = "来料检验修改"
//                vm.getDetail(hashMap)
//            }
//            else -> {
//                vm.getDetailModel(hashMap)
//            }
//        }

    }

    private fun onTextResult(i: Int, s: String) {
        list[i].scz = s
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.btnPost clicks {
            when {
                v.etBhgsl.text.toString() == "" -> {
                    Toast.makeText(this, "请输入不合格数量", Toast.LENGTH_SHORT).show()
                }
                v.etJssl.text.toString() == "" -> {
                    Toast.makeText(this, "请输入拒收数量", Toast.LENGTH_SHORT).show()
                }
                (total.toDouble() - v.etBhgsl.text.toString().toDouble()) < 0 -> {
                    Toast.makeText(this, "不合格数量不能大于合格数量", Toast.LENGTH_SHORT).show()
                }
                (total.toDouble() - v.etJssl.text.toString().toDouble()) < 0 -> {
                    Toast.makeText(this, "拒收数量不能大于合格数量", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    var tpwjm = ""
                    postPhoto.forEach {
                        tpwjm += "$it,"
                    }
                    model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                    model.DJH = djh
                    model.CGDDH = cgddh
                    model.ZJRWDH = rwbh
                    model.GSH = companyNo
                    model.WPH = v.tvWph.text.toString()
                    model.WPMC = v.tvWpmc.text.toString()
                    model.GG = v.tvGgms.text.toString()
                    model.JYJG = if (v.pass.isChecked) "1" else "2"
                    model.HGSL = if(v.etHgsl.text.toString().isEmpty()) 0.0 else v.etHgsl.text.toString().toDouble()
                    model.BHGSL =if(v.etBhgsl.text.toString().isEmpty()) 0.0 else v.etBhgsl.text.toString().toDouble()
                    model.JSSL =if(v.etJssl.text.toString().isEmpty()) 0.0 else v.etJssl.text.toString().toDouble()
                    model.SQMS = v.etJyms.text.toString()
                    model.DATA = list
                    model.JYYID = accout
                    model.JYSJ = v.tvRq.text.toString()
                    if (type == "modify") {
                        vm.modify(model)
                    } else {
                        vm.submit(model)
                    }
                }
            }
        }
    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["rwbh"] = rwbh
    }

    override fun initData() {

        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime

        hashMap = HashMap()
        list = ArrayList()

        if (intent.hasExtra("RWDH")) {
            rwbh = intent.getStringExtra("RWDH")
        }
        if (intent.hasExtra("cgddh")) {
            cgddh = intent.getStringExtra("cgddh")
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }

        v.tvRwbh.text = rwbh
        v.tvWph.text = intent.getStringExtra("wph")?:""
        v.tvWpmc.text = intent.getStringExtra("wpmc")?:""
        v.tvGgms.text = intent.getStringExtra("gg")?:""
        v.etHgsl.text = intent.getStringExtra("dhsl")?:"0"
        total = intent.getStringExtra("dhsl")?:"0"

        if(type !="modify")
        vm.getInspectionItems(v.tvWph.text.toString(), companyNo)

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
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

    override fun initVM() {
        vm.incomingDetail.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.tvWph.text = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvRwbh.text = it.data.list[0].RWBH
                v.etJyms.setText(it.data.list[0].SQMS)
                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ
                if(it.data.list[0].JYJG =="1"){
                    v.pass.isChecked = true
                }else{
                    v.unPass.isChecked = true
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

                v.etHgsl.text = it.data.list[0].HGSL
                v.etBhgsl.setText(it.data.list[0].BHGSL)

                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
                    list.addAll(it.data.BT)
                    println(list)
                    adapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
        vm.detailmodel.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.tvWph.text = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvRwbh.text = it.data.list[0].RWBH
                v.etHgsl.text = it.data.list[0].JYSL

                djh = it.data.list[0].DJH

                total = it.data.list[0].JYSL
                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
//                    list.addAll(it.data.BT.onEach {
//                        it.PDJG = "2"
//                    })
                    adapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
        vm.submitmodel.observe(this) {
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
        }
        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
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
                var conmap= intent.getStringExtra("conmap")
                if(conmap.isNullOrEmpty())
                    conmap ="lailiaojianyan"
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
        }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将权限请求结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("确定", "重要提示", "请去设置里开启权限！") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

}