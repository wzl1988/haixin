package com.eohi.hx.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.eohi.hx.utils.Extensions.showShortToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wangzl on 2019/6/20.
 */
object DateUtil {
    private const val DATE_FORMATE_YY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm"
    private const val DATE_FORMAT_YEAR_MONTH_DATE = "yyyy-MM-dd"

    private const val FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"

    fun compareTime(startDate: Date?, endDate: Date?): Boolean? {
        return startDate?.before(endDate)
    }

    fun formatDateTime(date: Date?): String {
        val df = SimpleDateFormat(FORMAT_DATETIME, Locale.SIMPLIFIED_CHINESE)
        return df.format(date)
    }

    fun formatDate(date: Date?): String {
        val df = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE)
        return df.format(date)
    }


    //"yyyy-MM-dd'T'HH:mm:ss.SSS"
    val dataTime: String
        get() {
            val df = SimpleDateFormat(DATE_FORMATE_YY_MM_DD_HH_MM, Locale.SIMPLIFIED_CHINESE)
            return df.format(Date())
        }

    val data: String
        get() {
            val df = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE, Locale.SIMPLIFIED_CHINESE)
            return df.format(Date())
        }

    val audioTime: String
        get() {
            val df = SimpleDateFormat(FORMAT_DATETIME, Locale.SIMPLIFIED_CHINESE)
            return df.format(Date())
        }

    val lastWeek: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.DATE, -7)
            val df = SimpleDateFormat(FORMAT_DATETIME, Locale.SIMPLIFIED_CHINESE)
            return df.format(calendar.time)
        }

    fun getNowTime(l: Long): String {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
        return df.format(Date(l))
    }

    fun getStrToDate(str: String?): Date? {
        val sdf = SimpleDateFormat(FORMAT_DATETIME, Locale.SIMPLIFIED_CHINESE)
        var date: Date? = null
        try {
            date = sdf.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun beginEndtime(firstTime: Date): String {
        return getTime(Date(), firstTime)
    }

    //????????????????????????? ??
    fun getTime(currentTime: Date, firstTime: Date): String {
        //???????????????
        val diff = currentTime.time - firstTime.time //????????????????????????????????????
        var result = ""
        try {
            //????????????
            val days = diff / (1000 * 60 * 60 * 24)
            //????????????
            val hours = diff % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
            //????????????
            val minutes = diff % (1000 * 60 * 60) / (1000 * 60)
            //?????????
            val seconds = diff % (1000 * 60) / 1000
            result = if (days == 0L) {
                hours.toString() + "???" + minutes + "???" + seconds + "???"
            } else if (days == 0L && hours == 0L) {
                minutes.toString() + "???" + seconds + "???"
            } else {
                days.toString() + "???" + hours + "???" + minutes + "???" + seconds + "???"
            }
        } catch (e: Exception) {
        }
        return result
    }

    //?????????
    val weekTime: String
        get() {
            val dateFormat =
                SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE, Locale.SIMPLIFIED_CHINESE)
            val calendar = Calendar.getInstance()
            calendar[Calendar.SECOND] = calendar[Calendar.SECOND] - 604800
            return dateFormat.format(calendar.time)
        }

    fun chooseStartDate(
        context: Context,
        container: TextView,
        startDate: Date?,
        endDate: Date?
    ): Date? {
        var chooseDate: Date? = null
        val timePickerView = TimePickerBuilder(
            context
        ) { date, _ ->
            if (startDate == null || endDate == null || compareTime(
                    startDate,
                    endDate
                )!!
            ) {
                chooseDate = date
                container.text = formatDateTime(date)
            } else {
                context.showShortToast("????????????????????????????????????")
            }
        }.setType(booleanArrayOf(true, true, true, true, true, true))//???????????????????????????????????????????????????
            .isDialog(true)
            .setCancelText("??????")
            .setSubmitText("??????")
            .build()

        val mDialog: Dialog = timePickerView.dialog
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
        )
        params.leftMargin = 20
        params.rightMargin = 20
        timePickerView.dialogContainerLayout.layoutParams = params
        val dialogWindow: Window? = mDialog.window
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //??????????????????
            dialogWindow.setGravity(Gravity.CENTER) //??????Bottom,????????????
        }
        timePickerView.show()
        return chooseDate
    }

    fun chooseEndDate(
        context: Context,
        container: TextView,
        startDate: Date?,
        endDate: Date?
    ): Date? {
        var chooseDate: Date? = null
        val timePickerView = TimePickerBuilder(
            context
        ) { date, _ ->
            if (startDate == null || endDate == null || compareTime(
                    startDate,
                    endDate
                )!!
            ) {
                chooseDate = date
                container.text = formatDateTime(date)
            } else {
                context.showShortToast("????????????????????????????????????")
            }
        }.setType(booleanArrayOf(true, true, true, true, true, true))//???????????????????????????????????????????????????
            .isDialog(true)
            .setCancelText("??????")
            .setSubmitText("??????")
            .build()

        val mDialog: Dialog = timePickerView.dialog
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
        )
        params.leftMargin = 20
        params.rightMargin = 20
        timePickerView.dialogContainerLayout.layoutParams = params
        val dialogWindow: Window? = mDialog.window
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //??????????????????
            dialogWindow.setGravity(Gravity.CENTER) //??????Bottom,????????????
        }
        timePickerView.show()
        return chooseDate
    }



    fun chooseDate(
        context: Context,
        container: TextView,
        startDate: Date?,
        endDate: Date?
    ): Date? {
        var chooseDate: Date? = null
        val timePickerView = TimePickerBuilder(
            context
        ) { date, _ ->
            if (startDate == null || endDate == null || compareTime(
                    startDate,
                    endDate
                )!!
            ) {
                chooseDate = date
                container.text = formatDate(date)
            } else {
                context.showShortToast("????????????????????????????????????")
            }
        }.setType(booleanArrayOf(true, true, true, false, false, false))//???????????????????????????????????????????????????
            .isDialog(true)
            .setCancelText("??????")
            .setSubmitText("??????")
            .build()

        val mDialog: Dialog = timePickerView.dialog
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
        )
        params.leftMargin = 20
        params.rightMargin = 20
        timePickerView.dialogContainerLayout.layoutParams = params
        val dialogWindow: Window? = mDialog.window
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //??????????????????
            dialogWindow.setGravity(Gravity.CENTER) //??????Bottom,????????????
        }
        timePickerView.show()
        return chooseDate
    }



}