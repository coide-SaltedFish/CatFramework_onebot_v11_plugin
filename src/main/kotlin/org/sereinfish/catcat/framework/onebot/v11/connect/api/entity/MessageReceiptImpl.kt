package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity

import kotlinx.coroutines.delay
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.OneBot

class MessageReceiptImpl(
    override val bot: OneBot,
    override val target: Contact,
    override val message: Message,
    override val messageId: UniversalId,
) : MessageReceipt {
    override suspend fun recall(time: Long): Boolean {
        delay(time)
        return bot.connect.api.deleteMsg(messageId)
    }
}