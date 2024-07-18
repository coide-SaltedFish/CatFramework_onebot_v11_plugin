package org.sereinfish.catcat.framework.onebot.v11.events.message

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.GroupMessageEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalMessageId

class OneBotGroupMessageEvent(
    bot: OneBot,
    time: Long,
    messageId: UniversalId,
    override val group: Group,
    override val sender: Member,
    override val message: OnlineMessageChain,
    override val rawMessage: String,
): GroupMessageEvent, OneBotMessageEvent(bot, time, messageId) {
    override val target: Contact = group

    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.MESSAGE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["message_type"].asString == "group"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time: Long = obj["time"].asLong
            val selfId = obj["self_id"].asLong.toUniversalId()
            val messageId = obj["message_id"].asInt.toUniversalMessageId()
            val targetId = obj["group_id"].asLong.toUniversalId()
            val senderId = obj["user_id"].asLong.toUniversalId()
            val message: JsonElement = obj["message"]
            val rawMessage: String = obj["raw_message"].asString

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[targetId] ?: error("无法找到对应群对象：$targetId")
            val sender = group.members[senderId] ?: error("无法找到对应群成员对象：$senderId")

            return OneBotGroupMessageEvent(
                bot = bot,
                time = time,
                group = group,
                sender = sender,
                messageId = messageId,
                rawMessage = rawMessage,
                message = bot.messageParser.parserOnline(bot, group, sender, messageId, message)
            )
        }
    }

    override suspend fun recall(time: Long): Boolean {
        return message.recall(time)
    }

    override fun toLogString(): String {
        return "$bot Group(${group.groupName}(${group.id})[${sender.cardNameOrRemarkNameOrNickName}(${sender.id})])" +
                " -> ${message.toLogString()}"
    }
}