package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Video
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.MultiMediaMessage
import org.sereinfish.catcat.framework.onebot.v11.message.element.video.OneBotOnlineVideo

abstract class OneBotVideo: Video, MultiMediaMessage {
    companion object: MessageParser {
        override val type: String = "video"

        override fun parse(data: JsonElement): SingleMessage {
            val obj = data.asJsonObject

            val file = obj["file"].asString
            val url = obj["url"].asString

            return OneBotOnlineVideo(file, url)
        }
    }
}