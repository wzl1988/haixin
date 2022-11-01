package com.eohi.hx.ui.main.model

data class AlarmlogModel(
    val DjLsh: Int,
    val RWID: String,
    val RZSJ: String,
    val alarmDesc: String,
    var alarmReadFlag: Int,
    val areaId: Int,
    val channelDeviceId: String,
    val channelName: String,
    val deviceName: String,
    val deviceNum: String
)