package org.sereinfish.catcat.framework.onebot.v11.events.message.send

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.send.GroupMessageSentEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.message.OneBotMessageEvent

class OneBotGroupMessageSentEvent(
    bot: OneBot,
    override val group: Group,
    override val sender: Bot,
    override val message: OnlineMessageChain,
): GroupMessageSentEvent, OneBotMessageEvent(bot, System.currentTimeMillis(), message.messageId) {
    override val target: Group = group
    override val rawMessage: String get() = message.encode().toJson()
    override suspend fun reply(message: Message): MessageReceipt {
        return this.message.reply(message)
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return group.sendMessage(message)
    }

    override fun toLogString(): String {
        return "$bot Group(${group.groupName}(${group.id}))" +
                " <- ${message.toLogString()}"
    }
}