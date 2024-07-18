package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.At
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotAt(
    override val target: UniversalId
) : At {

    companion object: MessageParser {
        override val type: String = "at"

        override fun parse(data: JsonElement): At {
            val atValue = data.asJsonObject["qq"].asString
            return if (atValue == "all") return OneBotAtAll()
            else OneBotAt(atValue.toLong().toUniversalId())
        }
    }

    override fun encode(): Any {
        return mapOf(
            "type" to type,
            "data" to mapOf(
                "qq" to target.toString()
            )
        )
    }
}