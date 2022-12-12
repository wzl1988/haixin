package com.eohi.hx.ui.work.adapter

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutPhotoItemBinding
import com.eohi.hx.ui.work.quality.delivery.DeliveryCheckActivity
import com.eohi.hx.ui.work.quality.delivery.DeliveryDetailActivity
import com.eohi.hx.ui.work.quality.finish.FinishCheckActivity
import com.eohi.hx.ui.work.quality.finish.FinishDetailActivity
import com.eohi.hx.ui.work.quality.first.FirstCheckActivity
import com.eohi.hx.ui.work.quality.first.FirstDetailActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.incoming.IncomingDetailActivity
import com.eohi.hx.ui.work.quality.process.ProcessCheckActivity
import com.eohi.hx.ui.work.quality.process.ProcessDetailActivity
import com.eohi.hx.ui.work.quality.storage.StorageActivity
import com.eohi.hx.ui.work.quality.storage.StorageDetailActivity
import java.io.File

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/8 16:00
 */
class ImageAdapter(context: AppCompatActivity, listDatas: ArrayList<String>) :
    BaseAdapter<LayoutPhotoItemBinding, String>(
        context,
        listDatas,
        RecyclerView.LayoutParams.WRAP_CONTENT
    ) {
    @Suppress("MISSING_DEPENDENCY_CLASS")
    override fun convert(v: LayoutPhotoItemBinding, t: String, position: Int) {
        Log.i("imageurl",t)
        if (mContext.javaClass == IncomingDetailActivity::class.java || mContext.javaClass == FirstDetailActivity::class.java
            || mContext.javaClass == ProcessDetailActivity::class.java || mContext.javaClass == FinishDetailActivity::class.java
            || mContext.javaClass == DeliveryDetailActivity::class.java||mContext.javaClass == StorageDetailActivity::class.java
        ) {
            Glide.with(mContext).load(t).into(v.ivItem)
        } else if ((mContext.javaClass == IncomingCheckActivity::class.java && IncomingCheckActivity.type == "modify") ||
            (mContext.javaClass == FirstCheckActivity::class.java && FirstCheckActivity.type == "modify") ||
            (mContext.javaClass == ProcessCheckActivity::class.java && ProcessCheckActivity.type == "modify") ||
            (mContext.javaClass == FinishCheckActivity::class.java && FinishCheckActivity.type == "modify") ||
            (mContext.javaClass == DeliveryCheckActivity::class.java && DeliveryCheckActivity.type == "modify")
            || mContext.javaClass == StorageActivity::class.java && StorageActivity.type == "modify"
        ) {
            Glide.with(mContext).load(t).into(v.ivItem)
        } else {
            Glide.with(mContext).load(File(t)).into(v.ivItem)
        }
    }

}