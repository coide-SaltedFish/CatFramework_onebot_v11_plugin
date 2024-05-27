package org.sereinfish.catcat.framework.onebot.v11.events

import com.google.gson.JsonElement

interface EventParser {
    val type: OneBotEventType

    fun match(data: JsonElement): Boolean

    fun parser(data: JsonElement): OneBotEvent
}