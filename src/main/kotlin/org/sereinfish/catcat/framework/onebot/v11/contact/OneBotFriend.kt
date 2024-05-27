package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.OneBot

class OneBotFriend private constructor(
    override val bot: OneBot,
    override val id: Long,
    override val nickname: String,
    override val remarkNickname: String
): Friend {
    override val name: String = nickname

    companion object {
        fun builds(bot: OneBot): List<Friend> {
            // 获取好友信息
            val data = runBlocking { bot.connect.api.getFriendList() }.getOrThrow()
            return data.list.map {
                OneBotFriend(bot, it.userId, it.nickname, it.remark)
            }
        }
    }

    /**
     * 发起私聊
     */
    override suspend fun sendMessage(message: Message): MessageReceipt {
        return bot.connect.api.sendPrivateMsg(id, message).getOrThrow().parser(bot, message)
    }

    override fun toString(): String {
        return "Friend($name[$id])"
    }
}