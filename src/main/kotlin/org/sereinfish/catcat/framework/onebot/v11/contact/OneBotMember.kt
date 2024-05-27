package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.OneBot

class OneBotMember private constructor(
    override val bot: OneBot,
    override val group: Group,
    override val id: Long,
    override val nickname: String,
    override var cardName: String,
    override var specialTitle: String,
    val role: String,
): Member {
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
        return bot.connect.api.sendPrivateMsg(id, message).getOrThrow().parser(bot, message)
    }

    override suspend fun unmute(): Boolean {
        return group.mute(id, 0)
    }

    override fun toString(): String {
        return "Member($name[$id])"
    }
}