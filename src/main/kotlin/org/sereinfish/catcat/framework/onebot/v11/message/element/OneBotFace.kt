package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Face
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalFaceId
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotFace(
    override val id: UniversalId
) : Face {
    companion object: MessageParser {
        override val type: String = "face"

        override fun parse(data: JsonElement): SingleMessage {
            return OneBotFace(OneBotUniversalFaceId(data.asJsonObject["id"].asInt))
        }
    }

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "id" to id
            }
        }
    }
}