package com.eohi.hx.ui.work.quality.incoming

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityIncomingDetailBinding
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.InspectionItemAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.ui.work.model.InspectionitemModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class IncomingDetailActivity : BaseActivity<IncomingViewModel, ActivityIncomingDetailBinding>() {

    private var rwbh = ""
    private lateinit var hashMap: HashMap<String, String>
    private lateinit var adapter: InspectionItemAdapter
    private lateinit var list: ArrayList<InspectionitemModel>
    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()

    companion object {
        var type = "detail"
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        hashMap = HashMap()
        list = ArrayList()

        adapter = InspectionItemAdapter(this, list,  ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        initMap()
        vm.getDetail(hashMap)

    }

    private fun onTextResult(i: Int, s: String) {
        list[i].scz = s
    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["rwbh"] = rwbh
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("RWDH")) {
            rwbh = intent.getStringExtra("RWDH")
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
        intent.putExtra("type","detail")
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
                v.tvHgsl.text = it.data.list[0].HGSL
                v.tvBhgsl.text = it.data.list[0].BHGSL
                v.tvJyms.text = it.data.list[0].SQMS
                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ
                if (it.data.list[0].JYJG == "1") {
                    v.tvPdjg.text = "合格"
                    v.tvCheckState.text = "合格"
                    v.tvCheckState.setTextColor(R.color.qualified.asColor())
                } else {
                    v.tvPdjg.text = "不合格"
                    v.tvCheckState.text = "不合格"
                    v.tvCheckState.setTextColor(R.color.unqualified.asColor())
                }

                if (it.data.list[0].TPWJM != "") {
                    it.data.list[0].TPWJM.trim().split(",").forEach {
                        postPhoto.add(it)
                        mPicList.add("$apiurl/apidefine/image?filename=$it")
                    }
                }

                imgAdapter?.notifyDataSetChanged()

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
    }
}