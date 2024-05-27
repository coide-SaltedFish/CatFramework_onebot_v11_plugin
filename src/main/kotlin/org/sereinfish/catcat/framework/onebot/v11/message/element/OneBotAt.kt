package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.At
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser

class OneBotAt(
    override val target: Long
) : At {

    companion object: MessageParser {
        override val type: String = "at"

        override fun parse(data: JsonElement): At {
            val atValue = data.asJsonObject["qq"].asString
            return if (atValue == "all") return OneBotAtAll()
            else OneBotAt(atValue.toLong())
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