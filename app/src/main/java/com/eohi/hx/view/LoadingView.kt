package com.eohi.hx.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var rotateDegree = 0
    private var mNeedRotate = false

    //绑定到window的时候
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mNeedRotate = true
        post(object : Runnable {
            override fun run() {
                rotateDegree += 30
                rotateDegree = if (rotateDegree <= 360) rotateDegree else 0
                invalidate()
                //是否继续旋转
                if (mNeedRotate) {
                    postDelayed(this, 200)
                }
            }
        })
    }

    //从window中解绑
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mNeedRotate = false
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.rotate(rotateDegree.toFloat(), (width shr 1).toFloat(), (height shr 1).toFloat())
        super.onDraw(canvas)
    }

}