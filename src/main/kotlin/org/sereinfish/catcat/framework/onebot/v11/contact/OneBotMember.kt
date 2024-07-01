package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSentEvent
import org.sereinfish.catcat.framework.onebot.v11.message.buildMessageChain

class OneBotMember private constructor(
    bot: OneBot,
    override val group: Group,
    id: Long,
    nickname: String,
    override var cardName: String,
    override var specialTitle: String,
    val role: String,
): Member, OneBotUser(bot, id, nickname) {
    override var admin: Boolean = false
        get() = role == "admin"

    override val name: String = nickname
    override val remarkNickname: String = cardName

    companion object {
        internal fun build(bot: OneBot, group: Group, id: Long): OneBotMember {
            val data = runBlocking { bot.connect.api.getGroupMemberInfo(group.id, id) }.getOrThrow()
            return OneBotMember(bot, group, data.userId, data.nickname, data.cardName, data.title, data.role)
        }

        internal fun builds(bot: OneBot, group: Group): List<OneBotMember> {
            val data = runBlocking { bot.connect.api.getGroupMemberList(group.id) }.getOrThrow()
            return data.list.map {
                OneBotMember(bot, group, it.userId, it.nickname, it.cardName, it.title, it.role)
            }
        }
    }

    override suspend fun kick(allowRejoinAfterKickout: Boolean): Boolean {
        return group.kick(id, allowRejoinAfterKickout)
    }

    override suspend fun mute(time: Int): Boolean {
        return group.mute(id, time)
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

    override suspend fun unmute(): Boolean {
        return group.mute(id, 0)
    }

    override fun toString(): String {
        return "Member($name[$id])"
    }
}