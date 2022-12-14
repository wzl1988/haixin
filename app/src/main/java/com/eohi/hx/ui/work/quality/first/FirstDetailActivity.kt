package com.eohi.hx.ui.work.quality.first

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityFirstDetailBinding
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class FirstDetailActivity : BaseActivity<FirstCheckViewModel, ActivityFirstDetailBinding>() {

    private lateinit var hashMap: HashMap<String, String>
    private var gdh = ""
    private var rwbh = ""
    var mPicList = ArrayList<String>()
    var adapter: ImageAdapter? = null
    private lateinit var subAdapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    companion object {
        var type = "detail"
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        hashMap = HashMap()

        subAdapter = InspectionItemAdapter(this, list, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = subAdapter

        initMap()
        vm.getDetail(hashMap)
    }

    private fun onTextResult(i: Int, s: String) {

    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["gdh"] = gdh
        hashMap["rwbh"] = rwbh
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("gdh")) {
            gdh = intent.getStringExtra("gdh")?:""
        }
        if (intent.hasExtra("rwbh")) {
            rwbh = intent.getStringExtra("rwbh")
        }

        postPhoto.clear()

        adapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = adapter
        adapter?.itemClick {
            viewPluImg(it)
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
        intent.putExtra("type", "detail")
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

    override fun initVM() {
        vm.dataList.observe(this) { it ->
            if (it.code == 1000) {
                if (it.data.list.size > 0) {

                    v.tvGdh.text = gdh
                    v.tvWph.text = it.data.list[0].WPH
                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS
                    v.tvRwbh.text = it.data.list[0].RWBH
                    v.tvBhgsl.text = it.data.list[0].BHGSL
                    v.tvCzy.text = it.data.list[0].JYYXM
                    v.tvRq.text = it.data.list[0].SJSJ
                    v.tvJylx.text = it.data.list[0].JYLX?:""

                    if (type == "detail" || type == "modify") {
                        v.tvHgsl.text = it.data.list[0].HGSL

                        if (!it.data.list[0].TPWJM.isNullOrEmpty()) {
                            it.data.list[0].TPWJM.trim().split(",").forEach {
                                postPhoto.add(it)
                                mPicList.add("$apiurl/apidefine/image?filename=$it")
                            }
                        }

                        adapter?.notifyDataSetChanged()
                    } else {
                        v.tvHgsl.text = it.data.list[0].JYSL
                    }
                    v.tvJyms.text = it.data.list[0].SQMS

                    if (it.data.list[0].JYJG == "1" || it.data.list[0].JYJG=="??????") {
                        v.tvPdjg.text = "??????"
                        v.tvCheckState.text = "??????"
                        v.tvCheckState.setTextColor(R.color.qualified.asColor())
                        v.llBlxx.gone()
                    } else {
                        v.tvPdjg.text = "?????????"
                        v.tvCheckState.text = "?????????"
                        v.tvCheckState.setTextColor(R.color.unqualified.asColor())
                        v.llBlxx.show()
                    }

                    if (it.data.BLYY != null && it.data.BLYY.size > 0) {
                        var str = ""
                        it.data.BLYY.forEachIndexed { index, blxxBean ->
                            if (index != it.data.BLYY.size - 1) {
                                str += blxxBean.xxsm + ","
                            }
                        }
                        v.tvBlxx.text = str
                    }

                    if (it.data.BT.size > 0) {
                        v.cardZjxm.show()
                        list.clear()
                        list.addAll(it.data.BT)

                        subAdapter.notifyDataSetChanged()
                    } else {
                        v.cardZjxm.gone()
                    }
                }
            } else {
                showShortToast(it.msg)
            }
        }
    }

}