package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.OneBot

class OneBotUser private constructor(
    override val bot: OneBot,
    override val id: Long,
    override val nickname: String
): User {
    override val name: String = nickname

    companion object {
        internal fun build(bot: OneBot, id: Long): OneBotUser {
            // 获取用户信息
            val data = runBlocking { bot.connect.api.getStrangerInfo(id) }.getOrThrow()
            return OneBotUser(bot, data.userId, data.nickname)
        }
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return bot.connect.api.sendPrivateMsg(id, message).getOrThrow().parser(bot, message)
    }

    override fun toString(): String {
        return "User($name[$id])"
    }
}