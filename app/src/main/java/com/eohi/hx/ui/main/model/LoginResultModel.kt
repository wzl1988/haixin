package com.eohi.hx.ui.main.model

import com.eohi.hx.ui.login.model.LoginResult

data class LoginResultModel(
    val code: Int,
    val `data`: Data,
    val menus: List<Menu>,
    val msg: String
)

data class Data(
    val list: List<LoginResult>
)

data class Menu(
    val cdbh: String,
    val cdmc: String,
    val ifyqx: Int,
    val secondcd: List<Secondcd>?
) : java.io.Serializable{
    constructor():this("","",0,null)
}

data class Secondcd(
    val cdbh1: String,
    val cdmc1: String,
    val ifyqx1: Int,
    val threecd: List<Threecd>
) : java.io.Serializable

data class Threecd (
    val cdbh2: String,
    val cdmc2: String,
    val ifyqx2: Int,
    var ftpdjmc:String=""
)  : java.io.Serializable