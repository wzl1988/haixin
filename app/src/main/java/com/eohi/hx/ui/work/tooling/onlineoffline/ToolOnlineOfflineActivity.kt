package com.eohi.hx.ui.work.tooling.onlineoffline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityToolOnlineOfflineBinding
import com.eohi.hx.ui.work.adapter.ViewPagerAdapter
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class ToolOnlineOfflineActivity : BaseActivity<BaseViewModel,ActivityToolOnlineOfflineBinding>() {
    var sbbh:String =""
    var detail:ToolOnlineFragment?=null
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.include.toolbar.clicks {finish()  }
        v.include.title.text = "工装上下线"
        if (v.btnZx.isEnabled) {
            v.btnZx.isEnabled = false
            v.btnXx.isEnabled = true
            v.rlSub.visibility = View.GONE
        }

        v.btnZx.setOnClickListener(View.OnClickListener {
            if (v.btnZx.isEnabled) {
                v.btnZx.isEnabled = false
                v.btnXx.isEnabled = true
                v.viewpager.currentItem = 0
                v.rlSub.visibility = View.GONE
            }
        })
        v.btnXx.setOnClickListener(View.OnClickListener {
            if (v.btnXx.isEnabled) {
                v.btnZx.isEnabled = true
                v.btnXx.isEnabled = false
                v.viewpager.currentItem = 1
                v.rlSub.visibility = View.VISIBLE
            }
        })


        val picking = ToolOfflineFragment()
        detail= ToolOnlineFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(picking)
        fragmentList.add(detail!!)
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
                    0 -> if (v.btnZx.isEnabled) {
                        v.btnZx.isEnabled = false
                        v.btnXx.isEnabled = true
                        v.rlSub.visibility = View.GONE
                    }
                    1 -> if (v.btnXx.isEnabled) {
                        v.btnZx.isEnabled = true
                        v.btnXx.isEnabled = false
                        v.rlSub.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun initClick() {
        v.btnSubmit.clicks {
            detail?.submit()
        }
    }

    override fun initData() {
        v.tvCzr.text = username
        v.tvRq.text = DateUtil.audioTime

    }

    override fun initVM() {
    }

    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }
    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION ) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                if( v.tvJth.isFocused)
                v.tvJth.setText(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }



}