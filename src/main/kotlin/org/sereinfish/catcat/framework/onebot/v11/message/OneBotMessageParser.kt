package org.sereinfish.catcat.framework.onebot.v11.message

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.User
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.OnlineMessageChain
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.message.element.*
import java.util.concurrent.ConcurrentHashMap

class OneBotMessageParser {
    private val logger = logger()
    private val parsers = ConcurrentHashMap<String, MessageParser>()

    companion object {
        fun parse(data: JsonElement): MessageChain {
            return OneBotManager.list().firstOrNull()?.let {
                it.messageParser.parse(data)
            } ?: error("尚未登录任何Bot，无法解析消息")
        }
    }

    init {
        register(OneBotPlantText)
        register(OneBotAt)
        register(OneBotFace)
        register(OneBotImage)
        register(OneBotVideo)
        register(OneBotVoice)
        register(OneBotReply)
    }

    fun register(parser: MessageParser) = parsers.put(parser.type, parser)

    /**
     * 解析为普通消息
     */
    fun parse(data: JsonElement): MessageChain = buildMessageChain {
        data.asJsonArray.forEach {
            val type = it.asJsonObject["type"].asString
            val value = it.asJsonObject["data"]

            parsers[type]?.parse(value)?.let {
                + it
            } ?: run {
                logger.warn("未知的消息元素类型：$type")
                logger.debug("未知消息类型元数据：${it.toJson()}")
                + OneBotUnimplementedMessage(it.asJsonObject["data"].toJson())
            }
        }
    }

    /**
     * 解析为在线消息
     */
    fun parserOnline(bot: OneBot, target: Contact, sender: User, messageId: Int, data: JsonElement): OnlineMessageChain {
        val chain = OneBotOnlineMessageChain(bot, messageId, sender, target)
        data.asJsonArray.forEach {
            val type = it.asJsonObject["type"].asString
            val value = it.asJsonObject["data"]

            parsers[type]?.parse(value)?.let {
                chain.add(it)
            } ?: run {
                logger.warn("未知的消息元素类型：$type")
                logger.debug("未知消息类型元数据：${it.toJson()}")
                chain.add(OneBotUnimplementedMessage(it.asJsonObject["data"].toJson()))
            }
        }

        return chain
    }
}