package com.eohi.hx.ui.main.agv

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityAgvOperationBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.StatusBarUtil

class AgvOperationActivity : BaseActivity<BaseViewModel, ActivityAgvOperationBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnDetail.isEnabled = true
        }

        v.btnIn.setOnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnDetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        }
        v.btnDetail.setOnClickListener {
            if (v.btnDetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        }


        val picking = AgvInFragment()
        val pickingDetail = AgvInDetialFragment()
        val fragmentList = ArrayList<Fragment>()
        when (intent.getStringExtra("type")) {
            "yljy" -> {
                v.btnIn.text = "检验合格入库"
                v.tvTitle.text = "原料检验合格入库"
                pickingDetail.agvtype = "014"
                pickingDetail.ly = "检验合格入库"
            }
            "oqccp" -> {
                v.btnIn.text = "分拣入库"
                v.tvTitle.text = "OQC分拣后至成品仓库"
                pickingDetail.agvtype = "031"
                pickingDetail.ly = "分拣入库"
            }
            "wxjl" -> {
                v.btnIn.text = "外协检验入库"
                v.tvTitle.text = "外协检验区入成品仓库"
                pickingDetail.agvtype = "016"
                pickingDetail.ly = "外协检验入库"
            }


        }

        fragmentList.add(picking)
        fragmentList.add(pickingDetail)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 2

        //object 匿名内部类
        v.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> if (v.btnIn.isEnabled) {
                        v.btnIn.isEnabled = false
                        v.btnDetail.isEnabled = true
                    }
                    1 -> if (v.btnDetail.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnDetail.isEnabled = false
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initClick() {

    }

    override fun initData() {

    }

    override fun initVM() {

    }

}