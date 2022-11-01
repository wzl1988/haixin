package com.eohi.hx.ui.work.picking.move

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityPickingMoveBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.StatusBarUtil

/**
 * 领料移库
 */
class PickingMoveActivity : BaseActivity<BaseViewModel, ActivityPickingMoveBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)
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

        val type = intent.getStringExtra("type")
        val picking = PickingMoveFragment()
        val pickingDetail = PickingMoveDetailFragment()
        when (type) {
            "agvwxll" -> {
                v.btnIn.text = "外协领料"
                v.btnIndetail.text = "发料明细"
                v.tvTitle.text = "外协领料"
                pickingDetail.ywlx = "044"
            }
            "agvxsfh" -> {
                v.btnIn.text = "销售发货转移"
                v.btnIndetail.text = "转移明细"
                v.tvTitle.text = "销售发货转移"
                pickingDetail.ywlx = "045"
            }

        }


        val fragmentList = ArrayList<Fragment>()
        picking.type = type
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