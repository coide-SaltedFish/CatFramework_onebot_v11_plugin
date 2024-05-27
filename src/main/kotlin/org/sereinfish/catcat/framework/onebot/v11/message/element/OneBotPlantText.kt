package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.PlantText
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotPlantText(
    override val text: String
): PlantText {

    companion object: MessageParser {
        override val type: String = "text"

        override fun parse(data: JsonElement): SingleMessage {
            return OneBotPlantText(data.asJsonObject["text"].asString)
        }

    }

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "text" to text
            }
        }
    }
}
