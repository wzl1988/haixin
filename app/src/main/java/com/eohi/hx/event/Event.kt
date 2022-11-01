package com.eohi.hx.event

import org.greenrobot.eventbus.EventBus

object Event {
    fun getInstance(): EventBus {
        return EventBus.getDefault()
    }
}