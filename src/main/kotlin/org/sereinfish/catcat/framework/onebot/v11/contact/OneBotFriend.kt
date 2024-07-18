package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSentEvent
import org.sereinfish.catcat.framework.onebot.v11.message.buildMessageChain
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalId
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotFriend private constructor(
    bot: OneBot,
    id: OneBotUniversalId,
    nickname: String,
    override val remarkNickname: String
): Friend, OneBotUser(bot, id, nickname) {
    override val name: String = nickname

    companion object {
        fun builds(bot: OneBot): List<Friend> {
            // 获取好友信息
            val data = runBlocking { bot.connect.api.getFriendList() }.getOrThrow()
            return data.list.map {
                OneBotFriend(bot, it.userId.toUniversalId(), it.nickname, it.remark)
            }
        }
    }

    /**
     * 发起私聊
     */
    override suspend fun sendMessage(message: Message): MessageReceipt {
        EventManager.broadcast(OneBotPrivateSendingEvent(bot, if (message is MessageChain) message else buildMessageChain { +message }, bot, this))
        return bot.connect.api.sendPrivateMsg(id, message).getOrThrow().parser(bot, message).also {
            // 获取消息
            val retMessageInfo = bot.connect.api.getMsg(it.messageId).getOrThrow()
            val onlineMessage = bot.messageParser.parserOnline(bot, this, bot, it.messageId, retMessageInfo.message)
            // 广播事件
            EventManager.broadcast(OneBotPrivateSentEvent(bot, retMessageInfo.time, it.bot, onlineMessage, this))
        }
    }

    override fun toString(): String {
        return "Friend($name[$id])"
    }
}