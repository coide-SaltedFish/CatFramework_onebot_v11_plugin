package org.sereinfish.catcat.framework.onebot.v11.message.element

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.SingleMessage
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Forward
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.message.MessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.element.forward.OneBotOnlineForward
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

abstract class OneBotForward: Forward {
    companion object: MessageParser {
        override val type: String = "forward"

        override fun parse(data: JsonElement): SingleMessage {
            return OneBotOnlineForward(data.asJsonObject["id"].asString)
        }
    }

    override fun encode(): Any {

        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "message" to nodes.map {
                    it as OneBotNode
                    it.encode()
                }
            }
        }
    }

    override fun toLogString(): String {
        return "[转发消息]"
    }


    interface OneBotNode: Forward.Node {
        val userId: Long
        val nickname: String

        fun encode(): Any {
            return buildCatMap {
                "type" to "node"
                "data" to buildCatMap {
                    "user_id" to userId
                    "nickname" to nickname
                    "content" to message.encode()
                }
            }
        }
    }
}