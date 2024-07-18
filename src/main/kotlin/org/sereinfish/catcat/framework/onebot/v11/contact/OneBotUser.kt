package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSentEvent
import org.sereinfish.catcat.framework.onebot.v11.message.buildMessageChain
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalId

open class OneBotUser internal constructor(
    override val bot: OneBot,
    override val id: OneBotUniversalId,
    override val nickname: String
): User {
    override val name: String = nickname

    companion object {
        val client = OkHttpClient.Builder()
            .build()

        internal fun build(bot: OneBot, id: OneBotUniversalId): OneBotUser {
            // 获取用户信息
            val data = runBlocking { bot.connect.api.getStrangerInfo(id) }.getOrThrow()
            return OneBotUser(bot, OneBotUniversalId(data.userId), data.nickname)
        }
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        EventManager.broadcast(OneBotPrivateSendingEvent(bot, if (message is MessageChain) message else buildMessageChain { + message }, bot, this))
        return bot.connect.api.sendPrivateMsg(id, message).getOrThrow().parser(bot, message).also {
            // 获取消息
            val retMessageInfo = bot.connect.api.getMsg(it.messageId).getOrThrow()
            val onlineMessage = bot.messageParser.parserOnline(bot, this, bot, it.messageId, retMessageInfo.message)
            // 广播事件
            EventManager.broadcast(OneBotPrivateSentEvent(bot, retMessageInfo.time, it.bot, onlineMessage, this))
        }
    }

    override fun queryFaceImage(): ByteArray {
        val request = Request.Builder()
            .url("http://q.qlogo.cn/headimg_dl?dst_uin=$id&spec=640")
            .build()

        return client.newCall(request).execute().body?.use { it.bytes() } ?: error("获取头像失败")
    }

    override fun toString(): String {
        return "User($name[$id])"
    }
}