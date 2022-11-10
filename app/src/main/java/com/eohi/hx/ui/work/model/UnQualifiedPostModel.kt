package com.eohi.hx.ui.work.model

data class UnQualifiedPostModel(
    var gsh:String,
    var jgdybh:String,
    var jgdymc:String,
    var sbbh:String,
    var sbmc:String,
    var gxtxh:String,
    var gxbh:String,
    var gxmc:String,
    var scfs:String,
    var bhgzsl:Int,
    var bz:String,
    var lzkkh: String,
    var rwdh:String,
    var cjrid:String,
    var cjr:String,
    var scrid:String,
    var scr:String,
    var items: ArrayList<Items>
) {
    constructor() : this("","","","","","","","","",
        0,"",  "", "", "", "", "", "",  ArrayList()
    )
}


data class Items(
    var blxxbm: String, //不良编码
    var blxx: String,
    var sm:String,
    var blsl: Int,
    var zrgxh:String,
    var zrgxm:String,
    var scrid: String,
    var scr: String
){
    constructor() : this("", "","", 0, "","","", "")
}

