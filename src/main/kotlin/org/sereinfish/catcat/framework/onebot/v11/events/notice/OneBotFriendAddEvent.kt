package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.FriendAddEvent
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

class OneBotFriendAddEvent(
    override val bot: Bot,
    override val time: Long,
    override val userId: Long
) : FriendAddEvent, OneBotNoticeEvent() {
    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "friend_add"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong
            val userId = obj["user_id"].asLong

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")

            return OneBotFriendAddEvent(
                bot = bot,
                time = time,
                userId = userId
            )
        }
    }

    override fun toLogString(): String {
        return "$bot > 新的好友：$userId"
    }
}