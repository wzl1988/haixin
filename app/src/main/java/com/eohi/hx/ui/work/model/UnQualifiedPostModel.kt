package com.eohi.hx.ui.work.model

data class UnQualifiedPostModel(
    var lzkkh: String,
    var bhgsl: Int,
    var gxh: String,
    var gxsm: String,
    var zrrgh: String,
    var zrrxm: String,
    var zjryid: String,
    var zjry: String,
    var bz: String,
    var swrq: String,
    var blxx: ArrayList<BlxxBean>
) {
    constructor() : this(
        "", 0, "", "", "", "", "", "", "", "", ArrayList()
    )
}
