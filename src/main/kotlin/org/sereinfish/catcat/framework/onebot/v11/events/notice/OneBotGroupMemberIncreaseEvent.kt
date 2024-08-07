package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMemberIncreaseEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotGroupMemberIncreaseEvent(
    override val bot: Bot,
    override val time: Long,
    override val operator: Member,
    override val type: GroupMemberIncreaseEvent.IncreaseType,
    override val user: Member,
    override val group: Group
) : GroupMemberIncreaseEvent, OneBotGroupNoticeEvent() {

    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "group_increase"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong.toUniversalId()
            val subType = obj["sub_type"].asString

            val groupId = obj["group_id"].asLong.toUniversalId()
            val operatorId = obj["operator_id"].asLong.toUniversalId()
            val userId = obj["user_id"].asLong.toUniversalId()

            val type = when(subType) {
                "approve" -> GroupMemberIncreaseEvent.IncreaseType.APPROVE
                "invite" -> GroupMemberIncreaseEvent.IncreaseType.INVITE
                else -> error("未知的群成员增加类型：$subType")
            }

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[groupId] ?: error("无法找到对应群对象：$groupId")
            val operator = group.members[operatorId] ?: error("无法找到对应群成员对象：$operatorId")
            val user = group.members[userId] ?: error("无法找到对应群成员对象：$userId")

            return OneBotGroupMemberIncreaseEvent(
                bot = bot,
                time = time,
                operator = operator,
                type = type,
                user = user,
                group = group
            )
        }
    }

    override fun toLogString(): String {
        return "$bot $group > $user 加入群聊[$type]，操作者：$operator"
    }
}