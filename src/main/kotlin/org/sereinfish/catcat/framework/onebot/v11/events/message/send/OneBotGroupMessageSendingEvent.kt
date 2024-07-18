package org.sereinfish.catcat.framework.onebot.v11.events.message.send

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.send.GroupMessageSendingEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotQQEvent
import org.sereinfish.catcat.framework.onebot.v11.message.buildMessageChain

class OneBotGroupMessageSendingEvent(
    bot: Bot,
    override val message: MessageChain,
    override val group: Group,
    override val sender: Bot
): GroupMessageSendingEvent, OneBotQQEvent(bot, System.currentTimeMillis()) {

    constructor(bot: Bot, message: Message, group: Group, sender: Bot): this(bot, if (message is MessageChain) message else buildMessageChain { + message }, group, sender)

    override val target: Group = group

    override suspend fun reply(message: Message): MessageReceipt {
        error("无法对尚未发送成功的消息进行回复")
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        return target.sendMessage(message)
    }
}