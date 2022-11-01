package com.eohi.hx.ui.work.tooling.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/14 10:24
 */

data class ToolBackSubmit(
    val  rkyy : String,
    val  czrbh : String,
    val  gsh : String,
    val  kwh : String,
    val items: List<RkItem>
)
data class RkItem(
    val  dqszwz : String,
    var  gzbh: String
)