package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupPokeEvent
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotGroupPokeEvent(
    override val bot: Bot,
    override val time: Long,
    override val group: Group,
    override val sender: Member,
    override val target: Member
) : GroupPokeEvent, OneBotGroupNoticeEvent() {
    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "notify" && obj["sub_type"].asString == "poke"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong.toUniversalId()
            val groupId = obj["group_id"].asLong.toUniversalId()
            val senderId = obj["user_id"].asLong.toUniversalId()
            val targetId = obj["target_id"].asLong.toUniversalId()

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[groupId] ?: error("无法找到对应群对象：$groupId")
            val sender = group.members[senderId] ?: error("无法找到对应群成员对象：$senderId")
            val target = group.members[targetId] ?: error("无法找到对应群成员对象：$targetId")

            return OneBotGroupPokeEvent(
                bot = bot,
                time = time,
                group = group,
                sender = sender,
                target = target
            )
        }
    }

    override fun toLogString(): String {
        return "$bot $group > $sender 戳了戳 $target"
    }
}