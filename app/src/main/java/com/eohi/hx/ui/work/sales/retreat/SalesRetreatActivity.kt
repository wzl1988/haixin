package com.eohi.hx.ui.work.sales.retreat

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivitySalesRetreatBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.StatusBarUtil

class SalesRetreatActivity : BaseActivity<BaseViewModel,ActivitySalesRetreatBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this,resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this,true)
        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnIndetail.isEnabled = true
        }

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


        val infragment = SalesRetreatFragment()
        val detailfragment = SalesRetreatDetailFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(infragment)
        fragmentList.add(detailfragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 2

        //object 匿名内部类
        v.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
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
    }

    override fun initData() {
    }

    override fun initVM() {
    }

}