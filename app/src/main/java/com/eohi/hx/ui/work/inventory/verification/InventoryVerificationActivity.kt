package com.eohi.hx.ui.work.inventory.verification

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityInventoryVerificationBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

/*
* 库存盘点
* */
class InventoryVerificationActivity :
    BaseActivity<BaseViewModel, ActivityInventoryVerificationBinding>() {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnIndetail.isEnabled = true
        }

        val scanVerificationFragment = ScanVerificationFragment()
        val verificationDetailFragment = VerificationDetailFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(scanVerificationFragment)
        fragmentList.add(verificationDetailFragment)
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
                        v.btnIndetail.isEnabled = true
                    }
                    1 -> if (v.btnIndetail.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnIndetail.isEnabled = false
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.btnIn.setOnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnIndetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        }
        v.btnIndetail.setOnClickListener {
            if (v.btnIndetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnIndetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        }
    }

    override fun initData() {
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
    }

    override fun initVM() {

    }

}