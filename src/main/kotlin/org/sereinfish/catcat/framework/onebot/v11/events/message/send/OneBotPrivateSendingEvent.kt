package org.sereinfish.catcat.framework.onebot.v11.events.message.send

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.send.PrivateMessageSendingEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotQQEvent

class OneBotPrivateSendingEvent(
    bot: OneBot,
    override val message: MessageChain,
    override val sender: Bot,
    override val user: User
): PrivateMessageSendingEvent, OneBotQQEvent(bot, System.currentTimeMillis()) {
    override val target: User = sender
    override suspend fun reply(message: Message): MessageReceipt {
        error("无法对尚未发送成功的消息进行回复")
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return user.sendMessage(message)
    }
}