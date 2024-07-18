package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Video
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.MultiMediaMessage
import org.sereinfish.catcat.framework.onebot.v11.message.element.video.OneBotOnlineVideo

abstract class OneBotVideo: Video, MultiMediaMessage {
    companion object: MessageParser {
        private val logger = logger()
        override val type: String = "video"

        override fun parse(data: JsonElement): SingleMessage {
            val obj = data.asJsonObject

            val file = obj["file"].asString
            val url = if (obj.has("url"))
                obj["url"].asString
            else {
                obj["path"].asString
            }

            return OneBotOnlineVideo(file, url)
        }
    }
}