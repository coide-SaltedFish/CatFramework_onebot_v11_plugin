package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.FriendMessageRecallEvent
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

class OneBotFriendMessageRecallEvent(
    override val bot: Bot,
    override val time: Long,
    override val sender: Friend,
    override val messageId: Int,
    override val operator: User
) : FriendMessageRecallEvent, OneBotNoticeEvent() {
    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "friend_recall"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong
            val messageId = obj["message_id"].asInt
            val senderId = obj["user_id"].asLong

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val sender = bot.friends[senderId] ?: error("无法找到对应好友对象：$senderId")

            return OneBotFriendMessageRecallEvent(
                bot = bot,
                time = time,
                sender = sender,
                messageId = messageId,
                operator = sender
            )
        }
    }

    override fun toLogString(): String {
        return "$bot > $operator 撤回了一条消息：$messageId"
    }
}