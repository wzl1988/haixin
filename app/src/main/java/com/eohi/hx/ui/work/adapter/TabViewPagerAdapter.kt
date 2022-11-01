package com.eohi.hx.ui.work.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/24 19:31
 */
class TabViewPagerAdapter(fm: FragmentManager?, var fragmentList: List<Fragment>?,val title:List<String>) : FragmentPagerAdapter(fm!!){
    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return if (fragmentList != null) fragmentList!!.size else 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

}