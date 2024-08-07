package org.sereinfish.catcat.framework.onebot.v11.message

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalMessageId

internal class OneBotOnlineMessageChain(
    override val bot: OneBot,
    override val messageId: UniversalId,
    override val sender: User,
    override val target: Contact
): OnlineMessageChain, OneBotMessageChain() {
    companion object{
        private val logger = logger()
    }

    override suspend fun recall(time: Long): Boolean {
        return if (sender.id == bot.id)
            bot.connect.api.deleteMsg(messageId)
        else {
            if (target is Group && target.members[bot.id]?.admin == true) {
                bot.connect.api.deleteMsg(messageId)
            }else {
                logger.error("消息撤回失败，只有群主或管理员可以撤回群消息：$messageId")
                false
            }
        }
    }

    override suspend fun reply(message: Message): MessageReceipt {
        return target.sendMessage(bot.messageFactory().build {
            + reply(messageId)
            + message
        })
    }
}