package com.eohi.hx.ui.work.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eohi.hx.R
import com.eohi.hx.utils.Extensions.asColor

/**
 *@author: YH
 *@date: 2021/12/9
 *@desc: 自定义分割线
 */
class SimpleDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var dividerHeight = 0
    private var paint: Paint

    init {
        dividerHeight = context.resources.getDimensionPixelSize(R.dimen.divider_height)
        paint = Paint()
        paint.color = R.color.page_bg.asColor()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            val bottom = view.bottom + dividerHeight
            val rect = Rect(left, top, right, bottom)
            c.drawRect(rect, paint)
        }
    }

}