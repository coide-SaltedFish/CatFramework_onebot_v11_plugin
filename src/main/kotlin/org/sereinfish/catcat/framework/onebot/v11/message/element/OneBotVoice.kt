package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Voice
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.MultiMediaMessage
import org.sereinfish.catcat.framework.onebot.v11.message.element.voice.OneBotOnlineVoice

abstract class OneBotVoice: Voice, MultiMediaMessage {
    companion object: MessageParser {
        override val type: String = "record"

        override fun parse(data: JsonElement): SingleMessage {
            val obj = data.asJsonObject

            val file = obj["file"].asString
            val magic = if (obj.has("magic")) obj["magic"].asInt != 0 else false
            val url = obj["url"].asString

            return OneBotOnlineVoice(file, magic, url)
        }
    }
}