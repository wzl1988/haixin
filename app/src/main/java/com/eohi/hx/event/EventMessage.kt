package com.eohi.hx.event

class EventMessage @JvmOverloads constructor(
    var code: EventCode,
    var msg: String = "",
    var arg1: String = "",
    var arg2: Int = 0,
    var obj: Any? = null
)