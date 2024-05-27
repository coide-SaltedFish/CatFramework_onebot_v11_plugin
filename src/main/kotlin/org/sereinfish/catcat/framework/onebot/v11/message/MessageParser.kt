package org.sereinfish.catcat.framework.onebot.v11.message

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage

interface MessageParser {
    val type: String
    fun parse(data: JsonElement): SingleMessage
}