package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupAdminUnsetEvent
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

class OneBotGroupAdminUnsetEvent(
    override val bot: Bot,
    override val time: Long,
    override val user: Member,
    override val group: Group
): GroupAdminUnsetEvent, OneBotGroupNoticeEvent() {

    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "group_admin" && obj["sub_type"].asString == "unset"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong
            val groupId = obj["group_id"].asLong
            val userId = obj["user_id"].asLong

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[groupId] ?: error("无法找到对应群对象：$groupId")
            val user = group.members[userId] ?: error("无法找到对应群成员对象：$userId")

            return OneBotGroupAdminUnsetEvent(
                bot = bot,
                time = time,
                user = user,
                group = group
            )
        }
    }

    override fun toLogString(): String {
        return "$bot $group > 取消管理员 $user"
    }
}