package com.eohi.hx.ui.work.equipment.model

import android.os.Parcel
import android.os.Parcelable

data class MaintainListModel(
    val APYHH: String?,
    val APYHM: String?,
    val AZDD: String?,
    val BXDDJH: String?,
    val CJR: String?,
    val CJSJ: String?,
    val GZBWBM: String?,
    val SBBH: String?,
    val SBMC: String?,
    val SCWBSJ: String?,
    val SWH: String?,
    val XH: String?,
    val bjewm: String?,
    val gsh: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(APYHH)
        parcel.writeString(APYHM)
        parcel.writeString(AZDD)
        parcel.writeString(BXDDJH)
        parcel.writeString(CJR)
        parcel.writeString(CJSJ)
        parcel.writeString(GZBWBM)
        parcel.writeString(SBBH)
        parcel.writeString(SBMC)
        parcel.writeString(SCWBSJ)
        parcel.writeString(SWH)
        parcel.writeString(XH)
        parcel.writeString(bjewm)
        parcel.writeString(gsh)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MaintainListModel> {
        override fun createFromParcel(parcel: Parcel): MaintainListModel {
            return MaintainListModel(parcel)
        }

        override fun newArray(size: Int): Array<MaintainListModel?> {
            return arrayOfNulls(size)
        }
    }

}