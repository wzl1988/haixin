package com.eohi.hx.ui.work.equipment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
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
import com.eohi.hx.databinding.ActivityEquipmentFaultConfirmBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.equipment.model.FaultPost
import com.eohi.hx.ui.work.equipment.model.FaultType
import com.eohi.hx.ui.work.model.FileUploadResult
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.*
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.activity_equipment_fault_confirm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * 故障确认详情
 */
class EquipmentFaultConfirmActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentFaultConfirmBinding>(),
    EasyPermissions.PermissionCallbacks {

    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    var listDatas: ArrayList<FaultType>? = null
    private lateinit var faultPost: FaultPost
    private lateinit var PJSWH: String
    var mPicList = ArrayList<String>()
    var imgAdapter: ImageAdapter? = null
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        PJSWH = intent.getStringExtra("PJSWH")
        setTaskFaultType()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sbbh.text = Html.fromHtml(
                "<font color='#333333'>二维码:</font><font color='#666666'>"
                        + intent.getStringExtra("sbbh") + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            tv_wpmc_text.text = Html.fromHtml(
                "<font color='#333333'>设备名称:</font><font color='#666666'>"
                        + intent.getStringExtra("SBMC") + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            xh.text = Html.fromHtml(
                "<font color='#333333'>规格型号:</font><font color='#666666'>"
                        + intent.getStringExtra("XH") + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            wz.text = Html.fromHtml(
                "<font color='#333333'>安装地点:</font><font color='#666666'>"
                        + intent.getStringExtra("SBDQSZWZ") + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            sbbh.text = Html.fromHtml(
                "<font color='#333333'>二维码:</font><font color='#666666'>"
                        + intent.getStringExtra("sbbh") + "</font>"
            )
            tv_wpmc_text.text = Html.fromHtml(
                "<font color='#333333'>设备名称:</font><font color='#666666'>"
                        + intent.getStringExtra("SBMC") + "</font>"
            )
            xh.text = Html.fromHtml(
                "<font color='#333333'>规格型号:</font><font color='#666666'>"
                        + intent.getStringExtra("XH") + "</font>"
            )
            wz.text = Html.fromHtml(
                "<font color='#333333'>安装地点:</font><font color='#666666'>"
                        + intent.getStringExtra("SBDQSZWZ") + "</font>"
            )
        }

        v.etScgx.text = intent.getStringExtra("GZBM")
        v.gzmc.text = intent.getStringExtra("GZMC")
        v.jhgs.text = intent.getIntExtra("XDWCGS",0).toString()
        v.tvJjcd.text = intent.getStringExtra("JJCD")
        v.etDescribe.setText(intent.getStringExtra("GZQK"))

    }

    private fun postFault(faultPost: FaultPost) {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setFaultData(faultPost).enqueue(object : MyCallBack<Any>() {
                override fun failure(apiErrorModel: ApiErrorModel?) {
                }

                override fun success(t: Any?) {
                    EventBus.getDefault().post(EventMessage(EventCode.REFRESH))
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_LONG).show()
                    finish()
                }
            })
    }

    private fun setTaskFaultType() {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setTaskFaultType(intent.getStringExtra("sbbh"))
            .enqueue(object : MyCallBack<FatherList<FaultType>>() {
                override fun success(t: FatherList<FaultType>?) {
                    listDatas = ArrayList()
                    listDatas?.addAll(t?.list!!)
                    println(listDatas)
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {

                }
            })
    }

    override fun initClick() {
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.ivBack clicks { finish() }
        v.btnChooseJjcd clicks {
            showJJCD()
        }
        v.tvFaultCode clicks {
            showCompanyList()
        }
        v.btnPost clicks {
            when {
                TextUtils.isEmpty(v.etScgx.text) -> {
                    Toast.makeText(this, "请选择故障编码", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.tvJjcd.text) -> {
                    Toast.makeText(this, "请选择紧急程度", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.etDescribe.text) -> {
                    Toast.makeText(this, "请输入报修描述", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    faultPost = FaultPost()
                    faultPost.pjswh = PJSWH
                    faultPost.jjcd = v.tvJjcd.text.toString()
                    faultPost.gzbm = v.etScgx.text.toString()
                    faultPost.gzqrms = v.etDescribe.text.toString()
                    faultPost.whetherTj = if (v.pass.isChecked) "是" else "否"
                    faultPost.whetherFault = if (v.bad.isChecked) "是" else "否"
                    faultPost.userid = accout
                    faultPost.wxzt = "01"
                    postFault(faultPost)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EquipmentGuaranteeActivity.REQUEST_CODE && data != null) {
            val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
            mPicList.addAll(images)
            imgAdapter?.notifyDataSetChanged()
            images?.forEach {
                val file = File(it)

                val requestBody: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var conmap= intent.getStringExtra("conmap")
                if(conmap.isNullOrEmpty())
                    conmap ="guzhangqueren"
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

    private fun showJJCD() {
        val list = mutableListOf("紧急", "可缓")
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            tv_jjcd.text = list[which]
        }
        builder.create().show()
    }

    private fun showCompanyList() {
        if (listDatas == null || listDatas!!.isEmpty()) return
        val list: ArrayList<String> = ArrayList()
        listDatas!!.forEach {
            list.add(it.gzbm)
        }
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            et_scgx.text = listDatas!![which].gzbm
            gzmc.text = listDatas!![which].gzmc
            jhgs.text = listDatas!![which].xdwcgs.toString()
        }
        builder.create().show()
    }

    override fun initData() {
        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime

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
    }
}