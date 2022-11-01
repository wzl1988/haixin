package com.eohi.hx.ui.work.quality.first

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.model.CommonPostModel
import com.eohi.hx.ui.work.model.JylxBean
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
    private lateinit var adapter: ZjxmAdapter
    private lateinit var list: ArrayList<BtBean>
    private lateinit var hashMap: HashMap<String, String>
    private var model = CommonPostModel()
    var mPicList = ArrayList<String>()
    private var djh = ""
    private var gdh = ""
    private var rwbh = ""
    private var total = "100"
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()
    private var jylx = ""
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

        adapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }

        }, ::onTextResult)
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
                        v.etHgsl.setText(num)
                    }
                } else {
                    v.etHgsl.setText(total)
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
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        initMap()
        when (type) {
            "modify" -> {
                v.tvTitle.text = "首件检验修改"
                vm.getDetail(hashMap)
            }
            else -> {
                vm.getDataList(hashMap)
            }
        }

    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["gdh"] = gdh
        hashMap["rwbh"] = rwbh
    }

    private fun onTextResult(i: Int, s: String) {
        list[i].JYZ = s
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
                    model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                    model.DJH = djh
                    model.GDH = gdh
                    model.ZJRWDH = v.tvRwbh.text.toString()
                    model.GSH = companyNo
                    model.WPH = v.tvWph.text.toString()
                    model.WPMC = v.tvWpmc.text.toString()
                    model.GG = v.tvGgms.text.toString()
                    model.JYJG = if (v.pass.isChecked) "1" else "2"
                    model.HGSL = v.etHgsl.text.toString()
                    model.BHGSL = v.etBhgsl.text.toString()
                    model.SQMS = v.etJyms.text.toString()
                    model.DATA = list
                    model.BLYY = postBlxxList
                    model.JYYID = accout
                    model.JYLXM = jylx
                    model.JYSJ = v.tvRq.text.toString()
                    if (type == "modify") {
                        vm.modify(model)
                    } else {
                        vm.postCheckFirst(model)
                    }
                }
            }
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
                tempList.add(it.XXSM)
            }
            val dialog =
                MultiListDialog(this, "不良现象", tempList, object : MultiListDialog.MyListener {
                    override fun refreshActivity(listPosition: ArrayList<Int>) {
                        postBlxxList.clear()
                        var str = ""
                        listPosition.forEach {
                            str += blxxList[it].XXSM + ","
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

        if (intent.hasExtra("gdh")) {
            gdh = intent.getStringExtra("gdh")
        }
        if (intent.hasExtra("rwbh")) {
            rwbh = intent.getStringExtra("rwbh")
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }

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
            if (it.code == 1000) {
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
            }
        }
        vm.dataList.observe(this) { it ->
            if (it.code == 1000) {
                if (it.data.list.size > 0) {
                    if (type == "modify") {
                        v.tvGdh.text = gdh
                        if (it.data.BLYY != null && it.data.BLYY.size > 0) {
                            var str = ""
                            it.data.BLYY.forEachIndexed { index, blxxBean ->
                                str += if (index == it.data.BLYY.size - 1) {
                                    blxxBean.XXSM
                                } else {
                                    blxxBean.XXSM + ","
                                }
                            }
                            v.tvBlxx.text = str
                            postBlxxList = it.data.BLYY
                        }
                    } else {
                        djh = it.data.list[0].DJH
                        v.tvGdh.text = it.data.list[0].DJH
                    }
                    v.tvWph.text = it.data.list[0].WPH
                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS
                    v.tvRwbh.text = it.data.list[0].RWBH
                    v.tvSapddh.text = it.data.list[0].SAPDDH
                    v.etJyms.setText(it.data.list[0].SQMS)

                    if ((it.data.list[0].JYJG == "1")) {
                        v.pass.isChecked = true
                    } else if ((it.data.list[0].JYJG == "2")) {
                        v.unPass.isChecked = true
                    }

                    total = if (type == "modify") {
                        (it.data.list[0].HGSL.toDouble() + it.data.list[0].BHGSL.toDouble()).toString()
                    } else {
                        it.data.list[0].JYSL
                    }

                    v.etBhgsl.setText(it.data.list[0].BHGSL)
                    if (type == "modify") {
                        v.etHgsl.text = it.data.list[0].HGSL

                        if (it.data.list[0].TPWJM != "") {
                            it.data.list[0].TPWJM.trim().split(",").forEach {
                                postPhoto.add(it)
                                mPicList.add("$apiurl/apidefine/image?filename=$it")
                            }
                        }

                        imgAdapter?.notifyDataSetChanged()
                    } else {
                        v.etHgsl.text = it.data.list[0].JYSL
                    }

                    if (it.data.BT.size > 0) {
                        v.cardZjxm.show()
                        list.clear()

                        if (type == "detail" || type == "modify") {
                            list.addAll(it.data.BT)
                        } else {
                            list.addAll(it.data.BT.onEach {
                                it.PDJG = "2"
                            })
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        v.cardZjxm.gone()
                    }
                }
            } else {
                showShortToast(it.msg)
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