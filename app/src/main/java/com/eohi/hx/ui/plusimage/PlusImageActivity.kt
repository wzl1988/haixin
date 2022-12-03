package com.eohi.hx.ui.plusimage

import android.content.DialogInterface
import android.graphics.Color
import android.view.KeyEvent
import androidx.viewpager.widget.ViewPager
import com.eohi.hx.App.Companion.postPhoto
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityPlusImageBinding
import com.eohi.hx.utils.Extensions.gone
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

class PlusImageActivity : BaseActivity<BaseViewModel, ActivityPlusImageBinding>(),
    ViewPager.OnPageChangeListener {

    companion object {
        const val IMG_LIST = "img_list" //第几张图片
        const val POSITION = "position" //第几张图片
        const val RESULT_CODE_VIEW_IMG = 11 //查看大图页面的结果码
        const val REQUEST_CODE_MAIN = 10 //请求码
    }
    private var postList //上传图片的数据源
            : ArrayList<String>? = null
    private var imgList //图片的数据源
            : ArrayList<String>? = null
    private var mPosition //第几张图片
            = 0
    private var mAdapter: ViewPagerAdapter? = null
    override fun isNeedEventBus(): Boolean {
        return false
    }

    private var type = ""

    override fun initView() {
        StatusBarUtil.setColor(this, Color.parseColor("#373b3e"))
        imgList = intent.getStringArrayListExtra(IMG_LIST)
        if(intent.hasExtra("POSTPIC")){
            postList = intent.getStringArrayListExtra("POSTPIC")
        }

        mPosition = intent.getIntExtra(POSITION, 0)

        mAdapter = ViewPagerAdapter(this, imgList!!)
        v.viewPager.also {
            it.addOnPageChangeListener(this)
            it.adapter = mAdapter
            it.currentItem = mPosition
        }
        v.positionTv.text = (mPosition + 1).toString() + "/" + imgList!!.size

    }

    override fun initClick() {
        v.backIv.clicks {
            back()
        }
        v.deleteIv.clicks {
            deletePic()
        }
    }

    override fun initData() {
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (type == "detail") {
            v.deleteIv.gone()
        }
//        if (IncomingDetailActivity.type == "detail" || FirstInsHandleActivity.type == "detail"
//            || ProcessCheckDetailActivity.type == "detail" || ProcessCheckDetailActivity.type == "detail"
//            || FinishCheckDetailActivity.type == "detail" || DeliveryCheckDetailActivity.type == "detail"
//        ) {
//            v.deleteIv.gone()
//        }
    }

    override fun initVM() {

    }

    //删除图片
    private fun deletePic() {
        val normalDialog: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(mContext)
        //        normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("确定要删除这张图片?")
        normalDialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { dialog, which ->
                imgList!!.removeAt(mPosition) //从数据源移除删除的图片
                if (postPhoto.size > 0 && mPosition<postPhoto.size) {
                    postPhoto.removeAt(mPosition)
                }
                postList?.removeAt(mPosition)
                setPosition()
                dialog.dismiss()
                if (imgList!!.size == 0) {
                    back()
                }
            })
        normalDialog.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        // 显示
        normalDialog.show()
    }

    //设置当前位置
    private fun setPosition() {
        v.positionTv.text = (mPosition + 1).toString() + "/" + imgList!!.size
        v.viewPager.currentItem = mPosition
        mAdapter!!.notifyDataSetChanged()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        mPosition = position
        v.positionTv.text = ((position + 1).toString() + "/" + imgList!!.size)
    }


    //返回上一个页面
    private fun back() {
        val intent = intent
        intent.putStringArrayListExtra(IMG_LIST, imgList)
        intent.putStringArrayListExtra("POSTPIC", postList)
        setResult(RESULT_CODE_VIEW_IMG, intent)
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //按下了返回键
            back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}