package com.eohi.hx.ui.work.mould

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityMouldInBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import java.util.*

class MouldInActivity : BaseActivity<BaseViewModel, ActivityMouldInBinding>() {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnDetail.isEnabled = true
        }

        v.btnIn.setOnClickListener(View.OnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnDetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        })
        v.btnDetail.setOnClickListener(View.OnClickListener {
            if (v.btnDetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        })


        val infragment = MouldInFragment()
        val detailfragment = MouldInDetailFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(infragment)
        fragmentList.add(detailfragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 2

        //object 匿名内部类
        v.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
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