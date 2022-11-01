package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description: 外协开工提交
 * @date :2021/6/25 17:08
 */
data class WxkgSubmitModel(
    val gysh: String,
    val items: List<Item>,
    val userid: String
)

data class Item(
    val  gxh : String,
    val  lzkkh : String,
    val  sybzs : Int
)