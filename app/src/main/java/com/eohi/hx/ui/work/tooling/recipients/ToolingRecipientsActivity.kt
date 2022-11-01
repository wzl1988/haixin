package com.eohi.hx.ui.work.tooling.recipients

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityToolingRecipientsNewBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.StatusBarUtil
import kotlinx.android.synthetic.main.common_toolbar.view.*

class ToolingRecipientsActivity :
    BaseActivity<BaseViewModel, ActivityToolingRecipientsNewBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "工装领用"
        v.root.toolbar.setNavigationOnClickListener {
            finish()
        }
        if (v.btnLy.isEnabled) {
            v.btnLy.isEnabled = false
            v.btnDetail.isEnabled = true
        }

        v.btnLy.setOnClickListener(View.OnClickListener {
            if (v.btnLy.isEnabled) {
                v.btnLy.isEnabled = false
                v.btnDetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        })
        v.btnDetail.setOnClickListener(View.OnClickListener {
            if (v.btnDetail.isEnabled) {
                v.btnLy.isEnabled = true
                v.btnDetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        })


        val picking = ToolingRecipientsFragment()
        val pickingDetail = ToolingRecipientsListFragement()
        val fragmentList = ArrayList<Fragment>()
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
                    0 -> if (v.btnLy.isEnabled) {
                        v.btnLy.isEnabled = false
                        v.btnDetail.isEnabled = true
                    }
                    1 -> if (v.btnDetail.isEnabled) {
                        v.btnLy.isEnabled = true
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