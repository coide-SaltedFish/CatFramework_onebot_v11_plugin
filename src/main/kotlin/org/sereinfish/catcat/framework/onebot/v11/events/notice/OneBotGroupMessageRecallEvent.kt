package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMessageRecallEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.MessageRecallEvent
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

class OneBotGroupMessageRecallEvent(
    override val bot: Bot,
    override val time: Long,
    override val group: Group,
    override val messageId: Int,
    override val operator: Member,
    override val sender: Member
) : GroupMessageRecallEvent, OneBotGroupNoticeEvent() {
    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "group_recall"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject
            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong
            val groupId = obj["group_id"].asLong
            val messageId = obj["message_id"].asInt
            val operatorId = obj["operator_id"].asLong
            val senderId = obj["user_id"].asLong

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[groupId] ?: error("无法找到对应群对象：$groupId")
            val operator = group.members[operatorId] ?: error("无法找到对应群成员对象：$operatorId")
            val sender = group.members[senderId] ?: error("无法找到对应群成员对象：$senderId")

            return OneBotGroupMessageRecallEvent(
                bot = bot,
                time = time,
                group = group,
                messageId = messageId,
                operator = operator,
                sender = sender
            )
        }
    }

    override fun toLogString(): String {
        return "$bot $group > $operator 撤回了一条来自 $sender 的消息[$messageId]"
    }
}