package com.eohi.hx.ui.plusimage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.eohi.hx.R

import com.github.chrisbanes.photoview.PhotoView

class ViewPagerAdapter(
    private val context: Context,
    //图片的数据源
    private val imgList: List<String>
) : PagerAdapter() {

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return imgList.size
    }

    //判断当前的View 和 我们想要的Object(值为View) 是否一样;返回 true/false
    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    @Suppress("MISSING_DEPENDENCY_CLASS")
    //instantiateItem()：将当前view添加到ViewGroup中，并返回当前View
    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): Any {
        val itemView = getItemView(R.layout.view_pager_img)
        val imageView =
            itemView.findViewById<View>(R.id.img_iv) as PhotoView
        Glide.with(context).load(imgList[position]).into(imageView)
        container.addView(itemView)
        return itemView
    }

    //destroyItem()：删除当前的View;  
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    private fun getItemView(layoutId: Int): View {
        return LayoutInflater.from(context).inflate(layoutId, null, false)
    }

}