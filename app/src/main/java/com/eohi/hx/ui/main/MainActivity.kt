package com.eohi.hx.ui.main

import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityMainBinding
import com.eohi.hx.utils.StatusBarUtil

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
    }

    override fun initData() {
        val workFragment = WorkFragment()
        val agvfragment = AgvFragment()
        val list: ArrayList<Fragment> = ArrayList()
        list.add(workFragment)
        list.add(agvfragment)
        list.add(MyFragment())

        val adapter = MyFragmentPagerAdapter(supportFragmentManager, list)
        v.viewpager.offscreenPageLimit = 0
        v.viewpager.adapter = adapter

        val menuhome = v.navigation.menu.findItem(R.id.navigation_practice)
        menuhome.setIcon(R.mipmap.ic_work_select)
        v.navigation.setOnNavigationItemSelectedListener {
            resetToDefaultIcon()
            when (it.itemId) {
                R.id.navigation_practice -> {
                    it.setIcon(R.mipmap.ic_work_select)
                    v.viewpager.setCurrentItem(0, false)
                }
                R.id.navigation_task -> {
                    it.setIcon(R.mipmap.ic_agv_select)
                    v.viewpager.setCurrentItem(1, false)
                }
                R.id.navigation_my -> {
                    it.setIcon(R.mipmap.ic_my_select)
                    v.viewpager.setCurrentItem(2, false)
                }
                else -> {

                }
            }

            return@setOnNavigationItemSelectedListener true
        }


    }


    private fun resetToDefaultIcon() {
        //主页
        val itemhome: MenuItem = v.navigation.menu.findItem(R.id.navigation_practice)
        //任务
        val itemTask: MenuItem = v.navigation.menu.findItem(R.id.navigation_task)
        //我的
        val itemMy: MenuItem = v.navigation.menu.findItem(R.id.navigation_my)
        itemhome.setIcon(R.mipmap.ic_work_normal)
        itemTask.setIcon(R.mipmap.ic_agv_normal)
        itemMy.setIcon(R.mipmap.ic_my_normal)
    }


    override fun initVM() {
    }


    inner class MyFragmentPagerAdapter(
        fm: FragmentManager?,
        list: List<Fragment>
    ) :
        FragmentPagerAdapter(fm!!) {
        var list: List<Fragment>
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(arg0: Int): Fragment {
            return list[arg0]
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
//            super.destroyItem(container, position, object);
        }

        init {
            this.list = list
        }
    }

}