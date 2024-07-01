package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.JsonMessage
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser

class OneBotJsonMessage(
    override val data: String
): JsonMessage {

    companion object: MessageParser {
        override val type: String = "json"

        override fun parse(data: JsonElement): OneBotJsonMessage {
            return OneBotJsonMessage(data.asJsonObject["data"].asString)
        }
    }

    override fun encode(): Any {
        return mapOf(
            "type" to type,
            "data" to mapOf(
                "data" to data
            )
        )
    }
}