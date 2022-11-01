package com.eohi.hx.ui.work.equipment.model

data class FaultPost(
    var pjswh: String,
    var jjcd: String,
    var userid: String,
    var whetherFault: String,
    var wxzt: String,
    var gzbm: String,
    var gzqrms: String,
    var whetherTj: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}
