package org.sereinfish.catcat.framework.onebot.v11.events.message.send

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.send.PrivateMessageSentEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.message.OneBotMessageEvent

class OneBotPrivateSentEvent(
    bot: OneBot,
    time: Long,
    override val sender: Bot,
    override val message: OnlineMessageChain,
    override val user: User
): PrivateMessageSentEvent, OneBotMessageEvent(bot, time, message.messageId) {
    override val target: User = user
    override val rawMessage: String = message.encode().toJson()

    override fun toLogString(): String {
        return "$bot $user <- ${message.toLogString()}"
    }

    override suspend fun reply(message: Message): MessageReceipt {
        return this.message.reply(message)
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return user.sendMessage(message)
    }
}