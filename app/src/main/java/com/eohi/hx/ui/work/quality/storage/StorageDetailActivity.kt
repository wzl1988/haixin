package com.eohi.hx.ui.work.quality.storage

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityStorageDetailBinding
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class StorageDetailActivity :  BaseActivity<StorageViewModel, ActivityStorageDetailBinding>() {

    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()
    private var djh = ""

    companion object {
        var jydh = ""
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        getGDList()

        adapter = InspectionItemAdapter(this, list, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

    }

    private fun getGDList() {
        vm.getStorageCheckDetail(BaseActivity.companyNo, jydh, djh)
    }

    private fun onTextResult(i: Int, s: String) {

    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("GDH")) {
            jydh = intent.getStringExtra("GDH")?:""
        }
        if (intent.hasExtra("DJH")) {
            djh = intent.getStringExtra("DJH")?:""
        }
        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    override fun initVM() {
        vm.detailmodel.observe(this) { it ->

            if (it.data.BT.size > 0) {
                v.cardZjxm.show()
                list.clear()
                list.addAll(it.data.BT)
                adapter.notifyDataSetChanged()
            } else {
                v.cardZjxm.gone()
            }

            if (it.data.list.size > 0) {
                v.tvGdh.text = it.data.list[0].GDH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvSapddh.text = it.data.list[0].SAPDDH
                v.tvHgsl.text = it.data.list[0].HGSL
                v.tvBhgsl.text = it.data.list[0].BHGSL
                v.tvLzkh.text = it.data.list[0].LZKKH
                v.tvBgjllsh.text = it.data.list[0].BGJYID.toString()
                v.tvBgsl.text = it.data.list[0].BGSL.toString()
                v.tvGltmh.text = it.data.list[0].GLTMH
                if (it.data.list[0].JYJG == "1") {
                    v.tvPdjg.text = "合格"
                } else {
                    v.tvPdjg.text = "不合格"
                }

                if (it.data.list[0].TPWJM != "") {
                    it.data.list[0].TPWJM.trim().split(",").forEach {
                        App.postPhoto.add(it)
                        mPicList.add("${BaseActivity.apiurl}/apidefine/image?filename=$it")
                    }
                }

                imgAdapter?.notifyDataSetChanged()

                v.tvJyms.text = it.data.list[0].JYMS

                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ

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
        intent.putExtra("type", "detail")
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

}