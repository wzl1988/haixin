package com.eohi.hx.ui.work.equipment

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.api.ApiService
import com.eohi.hx.api.HttpUtil
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityEquipmentRepairBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.EquipmentMaintainHistoryAdapter
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.equipment.model.*
import com.eohi.hx.ui.work.model.FileUploadResult
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.*
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.retrofit.ApiErrorModel
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.MyCallBack
import com.eohi.hx.utils.retrofit.RetrofitUtil
import com.eohi.hx.view.DialogAlert
import com.eohi.hx.widget.clicks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * 设备维修 处理
 */

class EquipmentRepairActivity : BaseActivity<BaseViewModel, ActivityEquipmentRepairBinding>() {
    private var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    var adapter: EquipmentMaintainHistoryAdapter? = null
    var listDatas: ArrayList<EquMaintainHistory>? = null
    private var mHandler: Handler = Handler()
    private lateinit var dialog: DialogAlert
    private var startDate: Date? = null
    private var endDate: Date? = null
    var gz = ""
    var mPicList = ArrayList<String>()
    var imgAdapter: ImageAdapter? = null
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var postPhoto = ArrayList<String>()
    private val api = RetrofitUtil.builder.baseUrl(apiurl)
        .build()
        .create(ApiService::class.java)

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        val data = intent.getParcelableExtra<MaintainListModel>("model")
        v.tvWxbh.text = data.SWH
        v.tvSbbh.text = data.SBBH
        v.tvSbmc.text = data.SBMC
        v.tvGgxh.text = data.XH
        v.tvAzdd.text = data.AZDD
        v.tvWbsj.text = data.SCWBSJ
        getMaintainHistory()
    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvAdd.clicks {
            var isfinish = true
            for (i in listDatas!!.indices) {
                if (listDatas!![i].qczt == "签出") {
                    isfinish = false
                    break
                }
            }

            if (isfinish) {
                val uuid = UUID.randomUUID().toString()
                checkout(EquMainRecordModel(uuid, v.tvWxbh.text.toString(), username, accout))
            } else {
                showShortToast("有未完成维修时间记录，不能添加新的维修时间记录！")
            }

        }
        v.btnJg.clicks {
            var list: ArrayList<String> = ArrayList()
            list.add("自修完成")
            list.add("需委外维修")
            showCompanyList(list)
        }
        v.etWcsj.clicks {
            DateUtil.chooseStartDate(mContext, v.etWcsj, startDate, endDate)
        }
        v.rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_by -> {
                    gz = "保养不当"
                }
                R.id.rb_sybd -> {
                    gz = "使用不当"
                }
                R.id.rb_zc -> {
                    gz = "正常损耗"
                }
            }
        }
        v.imageAdd.clicks {
            takeCameraPermissions()
        }
        v.btnSubmit.clicks {
            var isfinish = true
            for (i in listDatas!!.indices) {
                if (listDatas!![i].qczt == "签出") {
                    isfinish = false
                    break
                }
            }
            if(!isfinish){
                showShortToast("有未完成维修时间记录")
                return@clicks
            }

            var wxtp = ""
            postPhoto.forEach {
                wxtp += "$it,"
            }
            val model = EquMainFinishModel(
                v.tvWxbh.text.toString(),
                v.etFxyy.text.toString(),
                gz,
                v.etJgms.text.toString(),
                accout,
                v.etWcsj.text.toString(),
                v.etWxff.text.toString(),
                wxtp
            )
            submit(model)
        }
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.llDetail.measure(width, height)
        val mOriengHeight = v.llDetail.measuredHeight
        var isopen = false
        v.llDetail.visibility = View.GONE
        v.tvDetail.setOnClickListener {
            if (v.tvGzbh.text.isEmpty() && v.tvWxbh.text.isNotEmpty()) {
                getDetail(v.tvWxbh.text.toString())
            }
            var anim: ValueAnimator? = null

            if (isopen) {
                isopen = false
                anim = ValueAnimator.ofInt(mOriengHeight, 0)
            } else {
                isopen = true
                anim = ValueAnimator.ofInt(0, mOriengHeight)
            }
            anim.addUpdateListener {
                v.llDetail.layoutParams.height = it.animatedValue as Int
                v.llDetail.requestLayout()
            }
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    v.llDetail.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (isopen) {
                        v.ivArrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        v.ivArrow.setImageResource(R.drawable.arrow_down);
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
            anim.duration = 200
            anim.start()

        }


    }


    fun getDetail(swh: String) {
        api.getEquMaintainDetail(swh)
            .enqueue(object : MyCallBack<FatherList<EquMainDetailModel>>() {
                override fun success(t: FatherList<EquMainDetailModel>?) {
                    val model = t!!.list?.get(0)
                    v.tvGzbh.text = model?.GZBM
                    v.tvGzmc.text = model?.GZMC
                    v.tvGzbw.text = model?.GZBWMC
                    v.tvEwm.text = model?.BJEWM
                    v.tvXdsj.text = model?.XDWCGS.toString()
                    v.tvJjcd.text = model?.JJCD

                }

                override fun failure(apiErrorModel: ApiErrorModel?) {

                }

            })
    }


    override fun initData() {
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
        dialog = DialogAlert(this, "确定要删除此维修记录？")
        listDatas = ArrayList()
        adapter = EquipmentMaintainHistoryAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        adapter?.setOndeleteClick {
            dialog.setOnOkClick {
                removeRecord(EquMainRecordModel(listDatas!![it].checkno, "", "", ""), it)
            }
            dialog.show()
        }
        adapter?.setOnFinishclick {
            checkin(EquMainRecordModel(listDatas!![it].checkno, "", username, accout))
        }

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


    private fun submit(model: EquMainFinishModel) {
        api.equmaintainSubmit(model)
            .enqueue(object : MyCallBack<Any>() {
                override fun success(t: Any?) {
                    showShortToast("提交成功！")
                    v.etFxyy.setText("")
                    v.etJgms.text = ""
                    v.etWcsj.text = ""
                    v.etWxff.setText("")
                    mPicList.clear()
                    postPhoto.clear()
                    imgAdapter?.notifyDataSetChanged()
                    finish()
                    App.post(EventMessage(EventCode.REFRESH))
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }


    private fun removeRecord(model: EquMainRecordModel, i: Int) {
        api.removeMaintain(model)
            .enqueue(object : MyCallBack<Any>() {
                override fun success(t: Any?) {
                    listDatas?.removeAt(i)
                    adapter?.notifyDataSetChanged()
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }


    private fun checkin(model: EquMainRecordModel) {
        api.checkInMaintain(model)
            .enqueue(object : MyCallBack<Any>() {
                override fun success(t: Any?) {
                    getMaintainHistory()
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    private fun checkout(model: EquMainRecordModel) {
        api.checkoutMaintain(model)
            .enqueue(object : MyCallBack<Any>() {
                override fun success(t: Any?) {
                    getMaintainHistory()
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }


    private fun getMaintainHistory() {
        api.getEupMaintainHistory(v.tvWxbh.text.toString())?.enqueue(
            object : MyCallBack<FatherList<EquMaintainHistory>>() {
                override fun success(t: FatherList<EquMaintainHistory>?) {
                    listDatas?.clear()
                    listDatas?.addAll(t!!.list!!)
                    adapter?.notifyDataSetChanged()
                    mHandler.postDelayed(mRunnable, 1_000L)
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }

            }
        )
    }

    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (listDatas!!.isNotEmpty() && listDatas!![(listDatas!!.size - 1)].qczt == "签出") {
                listDatas!![(listDatas!!.size - 1)].qcsj = DateUtil.audioTime
                adapter?.notifyDataSetChanged()
                // 每隔一秒调用
                mHandler.postDelayed(this, 1_000L)
            }
        }
    }


    private fun showCompanyList(list: ArrayList<String>) {
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(list!!.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            v.etJgms.text = list[which]
        }
        builder.create().show()
    }


    override fun initVM() {
        resultFile.observe(this, androidx.lifecycle.Observer {
            postPhoto.add(it.list.toString())
        })
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
                    .addFormDataPart("workorderno", v.tvWxbh.text.toString())
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

    fun addImage(file: MultipartBody) {
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

    class LTranslateAnimation(
        private val mTargetView: View,
        private val mStartHeight: Int,
        private val mEndHeight: Int
    ) :
        Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            val newHeight = ((mEndHeight - mStartHeight)
                    * interpolatedTime + mStartHeight).toInt()
            mTargetView.layoutParams.height = newHeight
            mTargetView.requestLayout()
        }

        init {
            duration = 200
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }
}