package com.eohi.hx.ui.work.equipment.model

data class DailyCheck(
    var bjewm: String,
    var bz: String,
    var csh: String,
    var csmc: String,
    var djbwbh: String,
    var djbwewm: String,
    var djbwmc: String,
    var djjg: String,
    var djxmbh: String,
    var djxmmc: String,
    var gsh: String,
    var gsmc: String,
    var sbbh: String,
    var sbmc: String,
    var sbscrbsj: String,
    var sbscrbyh: String,
    var sbscrbyhh: String,
    var userid: String,
    var item: ArrayList<DailyCheckItem>
) {
    constructor() : this(
        "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ArrayList()
    )
}

data class DailyCheckItem(
    var djbwewm: String,
    var djbwbh: String,
    var djbwmc: String,
    var item: ArrayList<DailyCheckSubItem>
) {
    constructor() : this("", "", "", ArrayList())
}

data class DailyCheckSubItem(
    var djxmbh: String,
    var djxmmc: String,
    var djjg: String
) {
    constructor() : this("", "", "")
}