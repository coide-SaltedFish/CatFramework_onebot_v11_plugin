package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Image
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.MultiMediaMessage
import org.sereinfish.catcat.framework.onebot.v11.message.element.image.OneBotOnlineImage

abstract class OneBotImage: Image, MultiMediaMessage {
    companion object: MessageParser {
        private val logger = logger()
        override val type: String = "image"

        override fun parse(data: JsonElement): SingleMessage {
            return try {
                val obj = data.asJsonObject

                OneBotOnlineImage(
                    obj["file"].asString,
                    if (obj.has("type")) obj["type"].asString else "image",
                    obj["url"].asString
                )
            }catch (e: Exception) {
                logger.error("消息解析失败：${data.toJson()}")
                throw e
            }
        }
    }
}

