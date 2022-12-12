package com.eohi.hx.ui.work.quality.rejects.model

import android.os.Parcel
import android.os.Parcelable

data class RejectsDetermineSubmitModel(
    val swh: String,
    val wph:String,
    val swrq: String,
    val bhgzsl: Int,
    val blppdr: String,
    val blppdrid: String,
    val blppdsj: String,
    val bz: String,
    val cjr: String,
    val cjrid: String,
    val clbfList: List<Clbf>,
    val djr: String,
    val djrid: String,
    val gsh: String,
    val gxbh: String,
    val gxmc: String,
    val gxtxh: Int,
    val items: List<SubmitRejectsItem>,
    val jgdybh: String,
    val jgdymc: String,
    val lzkkh: String,
    val rwdh: String,
    val sbbh: String,
    val sbmc: String
)

data class SubmitRejectsItem (
    var blsl: Int,
    var blxx: String?,
    var blxxbm: String?,
    var blyy: String?,
    var blyybm: String?,
    var czfs: String?,
    var scr: String?,
    var scrid: String?,
    var sm: String?,
    var zrgxh: String?,
    var zrgxm: String?,
    var gxtxh:Int
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(blsl)
        parcel.writeString(blxx)
        parcel.writeString(blxxbm)
        parcel.writeString(blyy)
        parcel.writeString(blyybm)
        parcel.writeString(czfs)
        parcel.writeString(scr)
        parcel.writeString(scrid)
        parcel.writeString(sm)
        parcel.writeString(zrgxh)
        parcel.writeString(zrgxm)
        parcel.writeInt(gxtxh)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitRejectsItem> {
        override fun createFromParcel(parcel: Parcel): SubmitRejectsItem {
            return SubmitRejectsItem(parcel)
        }

        override fun newArray(size: Int): Array<SubmitRejectsItem?> {
            return arrayOfNulls(size)
        }
    }

}

data class Clbf(
    var clbfsl: Int,
    var clblxx: String,
    var clczfs: String,
    var clwph: String
)