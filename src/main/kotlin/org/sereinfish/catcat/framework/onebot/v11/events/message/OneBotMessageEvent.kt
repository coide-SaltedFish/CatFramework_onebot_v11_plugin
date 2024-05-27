package org.sereinfish.catcat.framework.onebot.v11.events.message

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.MessageEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotQQEvent

abstract class OneBotMessageEvent(
    bot: OneBot,
    time: Long,
    val messageId: Int,
): MessageEvent, OneBotQQEvent(bot, time) {

    override suspend fun reply(message: Message): MessageReceipt {
        return sendMessage(bot.messageFactory().build {
            + reply(messageId)
            + message
        })
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return target.sendMessage(message)
    }
}