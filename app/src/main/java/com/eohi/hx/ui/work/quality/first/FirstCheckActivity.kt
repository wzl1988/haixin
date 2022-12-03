package com.eohi.hx.ui.work.quality.first

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityFirstCheckBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.ui.work.quality.delivery.DeliveryDetailActivity.Companion.gdh
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity.Companion.REQUEST_CODE
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.MultiListDialog
import com.eohi.hx.view.MySpinnerAdapter
import com.eohi.hx.widget.clicks
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/*
* 首件检验
* */
class FirstCheckActivity : BaseActivity<FirstCheckViewModel, ActivityFirstCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    var imgAdapter: ImageAdapter? = null
    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    private lateinit var hashMap: HashMap<String, String>
    private var model = CheckPostModel()
    var mPicList = ArrayList<String>()
    private var djh = ""
    private var total = "100"
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()
    private var jylx = ""
    private var jylxmc =""
    private var jylxList = java.util.ArrayList<JylxBean>()
    private var jylxMcList = java.util.ArrayList<String>()
    private lateinit var jylxAdapter: ArrayAdapter<String>

    companion object {
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        hashMap = HashMap()

        vm.getJylx("45")
        vm.getBlxx()

        adapter = InspectionItemAdapter(this, list,::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        v.etBhgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (!TextUtils.isEmpty(v.etBhgsl.text.toString())) {
                    var num = ""
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

//        initMap()
//        when (type) {
//            "modify" -> {
//                v.tvTitle.text = "首件检验修改"
//                vm.getDetail(hashMap)
//            }
//            else -> {
//                vm.getDataList(hashMap)
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
                (total.toDouble() - v.etBhgsl.text.toString().toDouble()) < 0 -> {
                    Toast.makeText(this, "不合格数量不能大于合格数量", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    var tpwjm = ""
                    postPhoto.forEach {
                        tpwjm += "$it,"
                    }
                    model.tpwjm = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                    model.DATA = list
                    model.bhgsl = if(v.etBhgsl.text.toString().isEmpty())0.0 else v.etBhgsl.text.toString().toDouble()
                    model.cjbh= intent.getStringExtra("cjh")?:""
                    model.cjmc = intent.getStringExtra("cjmc")?:""
                    model.czr = username
                    model.czrid = accout
                    model.czrq = DateUtil.data
                    model.gg = v.tvGgms.text.toString()
                    model.gxh =  intent.getStringExtra("gxh")?:""
                    model.gxmc = intent.getStringExtra("gxmc")?:""
                    model.hgsl = if(v.etHgsl.text.toString().isEmpty())0.0 else v.etHgsl.text.toString().toDouble()
                    model.jgdybh = intent.getStringExtra("jgdybh")?:""
                    model.jgdymc = intent.getStringExtra("jgdymc")?:""
                    model.jyjg = if (v.pass.isChecked) "1" else "2"
                    model.jylx = jylx
                    model.jylxmc =jylxmc
                    model.jyms = v.etJyms.text.toString()
                    model.jysj = intent.getStringExtra("jysj")?:""
                    model.jysl = intent.getDoubleExtra("sjsl",0.0)
                    model.lzkkh = intent.getStringExtra("lzkh")?:""
                    model.rwdh = intent.getStringExtra("rwbh")?:""
                    model.sapddh = intent.getStringExtra("sapddh")?:""
                    model.wph = intent.getStringExtra("wph")?:""
                    model.wpmc = intent.getStringExtra("wpmc")?:""
                    model.zjrwdh = intent.getStringExtra("sjdh")?:""
                    vm.postCheckFirst(model)
                }
            }
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


        v.tvSjdh.text = intent.getStringExtra("sjdh")?:""
        v.tvLzkh.text = intent.getStringExtra("lzkh")?:""
        v.tvRwbh.text = intent.getStringExtra("rwbh")?:""
        v.tvScddh.text = intent.getStringExtra("sapddh")?:""
        v.tvWph.text = intent.getStringExtra("wpmc")?:""+"("+intent.getStringExtra("wph")?:""+")"
        v.tvGgms.text = intent.getStringExtra("gg")?:""
        v.tvGx.text = intent.getStringExtra("gxmc")?:""
        v.tvCj.text = intent.getStringExtra("cjmc")?:""
        v.tvJgdy.text = intent.getStringExtra("jgdymc")?:""
        v.tvSl.text = intent.getDoubleExtra("sjsl",0.0).toString()
        v.tvCjsj.text = intent.getStringExtra("jysj")?:""

        v.etHgsl.text =intent.getDoubleExtra("sjsl",0.0).toString()
        total = intent.getDoubleExtra("sjsl",0.0).toString()
        vm.getInspectionItems(intent.getStringExtra("wph")?:"", companyNo)

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
            }
        }
        vm.blxxList.observe(this) {
            blxxList.addAll(it)
        }
        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
        }
        vm.firstCheckPostResult.observe(this) {
            if (it.code == 200) {
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
            }
        }
        vm.dataList.observe(this) { it ->

        }

        vm.InspectionitemModelList.observe(this){
            if(it.isNotEmpty()){
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
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
                    conmap ="shoujianjianyan"
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("确定", "重要提示", "请去设置里开启权限！") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

}