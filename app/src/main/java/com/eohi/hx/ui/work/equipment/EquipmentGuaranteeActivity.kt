package com.eohi.hx.ui.work.equipment

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.Html
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.api.HttpUtil
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentGuaranteeBinding
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.equipment.model.EquipmentCheck
import com.eohi.hx.ui.work.equipment.model.Fault2
import com.eohi.hx.ui.work.equipment.model.FaultType
import com.eohi.hx.ui.work.equipment.model.PersonInfo
import com.eohi.hx.ui.work.model.FileUploadResult
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.*
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import kotlinx.android.synthetic.main.activity_equipment_guarantee.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * 故障报修
 */
class EquipmentGuaranteeActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentGuaranteeBinding>(),
    EasyPermissions.PermissionCallbacks {

    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    var listDatas: ArrayList<FaultType>? = ArrayList()
    var equipmentCheck: EquipmentCheck? = null
    private var zdqryhh = ""
    private var BJBH = ""
    private var BJMC = ""
    var mPicList = ArrayList<String>()
    var imgAdapter: ImageAdapter? = null
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()

    companion object {
        const val MAX_SELECT_PIC_NUM = 3
        const val REQUEST_CODE = 0x00000011
        const val RC_CAMERA = 0X01
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
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

    //查看大图
    private fun viewPluImg(position: Int) {
        val intent =
            Intent(this, PlusImageActivity::class.java)
        intent.putStringArrayListExtra(
            PlusImageActivity.IMG_LIST,
            mPicList
        )
        intent.putStringArrayListExtra(
            "POSTPIC",
            postPhoto
        )
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

    private fun takeCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
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
                IncomingCheckActivity.REQUEST_CODE,
                *perms
            )
        }
    }

    override fun initClick() {
        v.etLzkkh.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                actionId == EditorInfo.IME_ACTION_DONE || event != null
                && KeyEvent.KEYCODE_ENTER == event.keyCode
                && KeyEvent.ACTION_DOWN == event.action
            ) {
                setEquipment(v.etLzkkh.text.toString())
                setTaskFaultType(v.etLzkkh.text.toString())
            }
            false
        }
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.ivBack.clicks { finish() }
        v.btnPost.setOnClickListener {
            if (equipmentCheck == null)
                return@setOnClickListener
            if (et_scgx.text.isEmpty()) {
                Toast.makeText(mContext, "故障编码必选", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (et_bgsl.text.toString().isEmpty()) {
                Toast.makeText(mContext, "计划工时必填", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (et_level.text.isEmpty()) {
                Toast.makeText(mContext, "紧急程度必选", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var wxtp = ""
            postPhoto.forEach {
                wxtp += "$it,"
            }
            val f = Fault2(
                et_scgx.text.toString(),
                et_describe.text.toString(),
                wxtp,
                et_level.text.toString(),
                equipmentCheck!!.barcode,
                accout,
                et_bgsl.text.toString(),
                BJBH,
                BJMC,
                "",
                v.tvCzy.text.toString(),
                zdqryhh
            )
            setFault(f)
        }

        v.btnScgx.setOnClickListener {
            if (listDatas!!.size == 0)
                return@setOnClickListener
            val list: ArrayList<String> = ArrayList()
            listDatas!!.forEach {
                list.add(it.gzbm)
            }
            showCompanyList(list, 0)
        }
        v.bJjcd.setOnClickListener {
            val list: ArrayList<String> = ArrayList()
            list.add("紧急")
            list.add("可缓")
            showCompanyList(list, 1)
        }

        v.etLzkkh clicks {
            checkCameraPermissions()
        }
    }

    private fun setFault(lzkPostModel: Fault2) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setFault(lzkPostModel).enqueue(object : MyCallBack<Any>() {
                override fun failure(apiErrorModel: ApiErrorModel?) {
                }

                override fun success(t: Any?) {
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_LONG).show()
                    postPhoto.clear()
                    finish()
                }
            })
    }

    private fun showCompanyList(list: ArrayList<String>, type: Int) {

        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            if (type == 0) {
                v.etScgx.text = listDatas!![which].gzbm
                v.tvBgsl.text = listDatas!![which].gzmc
                v.etBgsl.setText(listDatas!![which].xdwcgs.toString())
            } else if (type == 1) {
                v.etLevel.text = list[which]
            }
        }
        builder.create().show()
    }

    private fun setEquipment(sbbh: String) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setEquipment(sbbh).enqueue(object : MyCallBack<FatherList<EquipmentCheck>>() {
                override fun success(t: FatherList<EquipmentCheck>?) {
                    if (t?.list?.size == 0) {
                        Toast.makeText(mContext, "信息错误", Toast.LENGTH_LONG).show()
                        return
                    }
                    equipmentCheck = t?.list?.get(0)
                    t?.list?.get(0)?.let {
                        BJBH = it.BJBH
                        BJMC = it.BJMC
                    }
                    getPersonInfo(equipmentCheck!!.CSH, sbbh)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tv_wpmc_text.text = Html.fromHtml(
                            "<font color='#333333'>设备编号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBBM + "</font>", Html.FROM_HTML_MODE_LEGACY
                        )
                        tv_gg_text.text = Html.fromHtml(
                            "<font color='#333333'>设备名称：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBMC + "</font>", Html.FROM_HTML_MODE_LEGACY
                        )
                        xh.text = Html.fromHtml(
                            "<font color='#333333'>规格型号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBXH + "</font>",
                            Html.FROM_HTML_MODE_LEGACY
                        )
                        dd.text = Html.fromHtml(
                            "<font color='#333333'>安装地点：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.DQSZWZ + "</font>",
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        tv_wpmc_text.text = Html.fromHtml(
                            "<font color='#333333'>设备编号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBBM + "</font>"
                        )
                        tv_gg_text.text = Html.fromHtml(
                            "<font color='#333333'>设备名称：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBMC + "</font>"
                        )
                        xh.text = Html.fromHtml(
                            "<font color='#333333'>规格型号：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.SBXH + "</font>"
                        )
                        dd.text = Html.fromHtml(
                            "<font color='#333333'>安装地点：</font><font color='#666666'>"
                                    + t?.list?.get(0)?.DQSZWZ + "</font>"
                        )
                    }
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    private fun getPersonInfo(csh: String, sbbh: String) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .getPersonInfo(companyNo, sbbh, csh)
            .enqueue(object : MyCallBack<FatherList<PersonInfo>>() {
                override fun success(t: FatherList<PersonInfo>?) {
                        if(t?.list!!.isNotEmpty()){
                            t?.list?.get(0)?.let {
                                v.tvCzy.text = it.ZDQRYHM
                                zdqryhh = it.ZDQRYHH
                            }
                        }
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {

                }

            })
    }

    private fun setTaskFaultType(sbbh: String) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setTaskFaultType(sbbh).enqueue(object : MyCallBack<FatherList<FaultType>>() {
                override fun success(t: FatherList<FaultType>?) {
                    listDatas?.clear()
                    listDatas?.addAll(t?.list!!)
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {

                }
            })
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
                    conmap ="shebeiweixiu"
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "filename",
                        file.name,
                        requestBody
                    )
                    .addFormDataPart("busclass", "1")
                    .addFormDataPart("acesstype", "1")
                    .addFormDataPart("conmap", conmap)
                    .addFormDataPart("workorderno", "")
                    .build()
                addImage(body)
            }
        } else if (requestCode == PlusImageActivity.REQUEST_CODE_MAIN && resultCode == PlusImageActivity.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            val toDeletePicList =
                data!!.getStringArrayListExtra(PlusImageActivity.IMG_LIST) //要删除的图片的集合
            mPicList.clear()
            mPicList.addAll(toDeletePicList)
            postPhoto.clear()
            postPhoto.addAll(data!!.getStringArrayListExtra("POSTPIC"))
            imgAdapter?.notifyDataSetChanged()
        } else if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.etLzkkh.setText(result)
                    setEquipment(result)
                    setTaskFaultType(result)
                }
            }
        }
    }

    private fun addImage(file: MultipartBody) {
        vm.viewModelScope.launch {
            try {
                vm.showLoading()
                val result = withContext(Dispatchers.IO) {
                    HttpUtil.getInstance().getService().fileUpload(file)
                }
                if (result.code == 200) {//请求成功
                    resultFile.value = result.data
                } else {
                    LogUtil.e("请求错误>>" + result.msg)
                    vm.showError(ErrorResult(result.code, result.msg, true))
                }
            } catch (e: Throwable) {//接口请求失败
                LogUtil.e("请求异常>>" + e.message)
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                vm.showError(errorResult)
            } finally {//请求结束
                vm.dismissLoading()
            }

        }

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
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun initData() {
        v.tvRq.text = DateUtil.audioTime
        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    override fun initVM() {
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
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etLzkkh.setText(result)
                setEquipment(result)
                setTaskFaultType(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}