package org.sereinfish.catcat.framework.onebot.v11.message

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Reply
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalMessageId

class OneBotReply(override val messageId: UniversalId) : Reply {

    companion object: MessageParser {
        override val type: String = "reply"

        override fun parse(data: JsonElement): Reply {
            return OneBotReply(data.asJsonObject["id"].asInt.toUniversalMessageId())
        }
    }

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "id" to messageId
            }
        }
    }

    override fun toLogString(): String {
        return "[回复:$messageId]"
    }
}