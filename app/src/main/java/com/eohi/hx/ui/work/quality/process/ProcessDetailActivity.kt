package com.eohi.hx.ui.work.quality.process

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProcessDetailBinding
import com.eohi.hx.ui.plusimage.PlusImageActivity
import com.eohi.hx.ui.work.adapter.ImageAdapter
import com.eohi.hx.ui.work.adapter.ZjxmAdapter
import com.eohi.hx.ui.work.model.BtBean
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.Extensions.show
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class ProcessDetailActivity : BaseActivity<ProcessViewModel, ActivityProcessDetailBinding>() {

    private lateinit var list: ArrayList<BtBean>
    private lateinit var adapter: ZjxmAdapter
    private var gdh = ""
    private var gxh = ""
    private var djh = ""
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

        list = ArrayList()

        adapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }
        }, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        vm.getDetail(companyNo, gdh, gxh, djh)
    }

    private fun onTextResult(i: Int, s: String) {

    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("GDH")) {
            gdh = intent.getStringExtra("GDH")
        }
        if (intent.hasExtra("GXH")) {
            gxh = intent.getStringExtra("GXH")
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

    override fun initVM() {
        vm.detailmodel.observe(this) {
            if (it.code == 1000) {
                if (it.data.BT.size <= 0) {
                    v.cardZjxm.gone()
                } else {
                    v.cardZjxm.show()
                    list.clear()
//                    list.addAll(it.data.BT)
                    adapter.notifyDataSetChanged()
                }
                if (it.data.list.size > 0) {
                    v.tvCx.text = it.data.list[0].CX
                    v.tvGdh.text = it.data.list[0].SCRWDH
                    v.tvWph.text = it.data.list[0].WPH
                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS
                    v.tvJylx.text = it.data.list[0].JYLXMC
                    v.tvSapddh.text =  it.data.list[0].SAPDDH

                    v.tvJygx.text = it.data.list[0].JYGX
                    if (it.data.list[0].JYJG == "1") {
                        v.tvPdjg.text = "合格"
                        v.llBlxx.gone()
                    } else {
                        v.tvPdjg.text = "不合格"
                        v.llBlxx.show()
                        if (it.data.BLYY != null && it.data.BLYY.size > 0) {
                            var str = ""
                            it.data.BLYY.forEachIndexed { index, blxxBean ->
                                str += if (index == it.data.BLYY.size - 1) {
                                    blxxBean.xxsm
                                } else {
                                    blxxBean.xxsm + ","
                                }
                            }
                            v.tvBlxx.text = str
                        }
                    }

                    v.tvHgsl.text = it.data.list[0].HGSL
                    v.tvBhgsl.text = it.data.list[0].BHGSL
                    v.tvJyms.text = it.data.list[0].JYMS

                    if (!TextUtils.isEmpty(it.data.list[0].TPWJM) && it.data.list[0].TPWJM != "") {
                        it.data.list[0].TPWJM.trim().split(",").forEach {
                            App.postPhoto.add(it)
                            mPicList.add("$apiurl/apidefine/image?filename=$it")
                        }
                    }

                    imgAdapter?.notifyDataSetChanged()

                    v.tvCzy.text = it.data.list[0].JYYXM
                    v.tvRq.text = it.data.list[0].JYSJ
                }
            } else {
                showShortToast(it.msg)
            }
        }
    }

}