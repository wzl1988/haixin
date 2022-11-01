package com.eohi.hx.ui.work.adapter

import android.app.Activity
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutImageItemBinding
import com.eohi.hx.ui.work.model.ImageViewModel
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/24 17:20
 */
class ImageViewAdapter (mContext: Activity, listDatas: ArrayList<ImageViewModel>)
    : BaseAdapter<LayoutImageItemBinding, ImageViewModel>(mContext,listDatas) {
    override fun convert(v: LayoutImageItemBinding, t: ImageViewModel, position: Int) {
        v.ivIcon.setImageResource(t.icon)
        v.tvText.text = t.str
        v.llLayout.clicks {
            onItemClick?.let {
                var str = t.url
                if(str.isNullOrEmpty())
                    str =""
                it(t.icon,str)
            }
        }
    }

    var onItemClick:((Int,String)->Unit)?= null

     fun onNewItemClick(itemClick: (Int,String) -> Unit) {
        this.onItemClick = itemClick
    }


}