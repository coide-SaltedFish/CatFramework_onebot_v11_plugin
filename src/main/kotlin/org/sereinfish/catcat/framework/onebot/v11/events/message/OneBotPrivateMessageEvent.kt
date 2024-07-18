package org.sereinfish.catcat.framework.onebot.v11.events.message

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.PrivateMessageEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotUser
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalMessageId

class OneBotPrivateMessageEvent(
    bot: OneBot,
    time: Long,
    messageId: UniversalId,
    override val type: PrivateMessageEvent.PrivateType,
    override val sender: User,
    override val message: OnlineMessageChain,
    override val rawMessage: String
): PrivateMessageEvent, OneBotMessageEvent(bot, time, messageId) {
    override val target: Contact = sender

    internal companion object: EventParser {
        private val logger = logger()
        override val type: OneBotEventType = OneBotEventType.MESSAGE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["message_type"].asString == "private"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            return try {
                val obj = data.asJsonObject

                val time: Long = obj["time"].asLong
                val selfId = obj["self_id"].asLong.toUniversalId()
                val subType: String = obj["sub_type"].asString
                val messageId = obj["message_id"].asInt.toUniversalMessageId()
                val senderId = obj["user_id"].asLong.toUniversalId()
                val message: JsonElement = obj["message"]
                val rawMessage: String = obj["raw_message"].asString

                val type: PrivateMessageEvent.PrivateType = when(subType) {
                    "friend" -> PrivateMessageEvent.PrivateType.FRIEND
                    "group" -> PrivateMessageEvent.PrivateType.GROUP
                    else -> PrivateMessageEvent.PrivateType.OTHER
                }

                val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")

                val sender = when(type) {
                    PrivateMessageEvent.PrivateType.FRIEND -> bot.friends[senderId] ?: OneBotUser.build(bot, senderId)
                    else -> OneBotUser.build(bot, senderId)
                }

                OneBotPrivateMessageEvent(
                    bot = bot,
                    time = time,
                    messageId = messageId,
                    type = type,
                    sender = sender,
                    rawMessage = rawMessage,
                    message = bot.messageParser.parserOnline(bot, sender, sender, messageId, message)
                )
            }catch (e: Exception){
                logger.error("解析失败：${data.toJson()}")
                throw e
            }
        }
    }

    override fun toLogString(): String {
        return "$bot $sender -> ${message.toLogString()}"
    }
}