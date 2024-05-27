package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.MessageReceiptImpl
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

/**
 * 消息回执
 */
data class MessageReceiptMateData(
    val messageId: Int
){
    companion object: ApiResponseParser<MessageReceiptMateData> {
        private val logger = logger()

        override fun parser(data: JsonElement): MessageReceiptMateData {
            return runCatching {
                MessageReceiptMateData(data.asJsonObject["data"].asJsonObject["message_id"].asInt)
            }.getOrElse {
                logger.error(data.toJson())
                throw runCatching {
                    Exception(data.asJsonObject["message"].asString)
                }.getOrNull() ?: it
            }
        }
    }

    fun parser(bot: OneBot, message: Message): MessageReceipt {
        return MessageReceiptImpl(bot, bot, message, messageId)
    }
}