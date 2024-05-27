package org.sereinfish.catcat.framework.onebot.v11

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.ExternalResource
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageFactory
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.connect.OneBotConnect
import org.sereinfish.catcat.framework.onebot.v11.contact.list.DynamicFriendList
import org.sereinfish.catcat.framework.onebot.v11.contact.list.DynamicGroupList
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventParser
import org.sereinfish.catcat.framework.onebot.v11.message.BuildMessageChain
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotExternalResource
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser
import java.io.InputStream

class OneBot(
    val connect: OneBotConnect
): Bot {
    private val logger = logger()
    override val bot: Bot = this

    override val friends: Map<Long, Friend> get() =  TODO()
    override val groups: Map<Long, Group>
    override val id: Long
    override val name: String

    val eventParser = OneBotEventParser()
    val messageParser = OneBotMessageParser()

    override val version: String = "OneBot_V11"

    init {
        // 获取基本信息
        logger.info("$this 正在初始化数据")

        val info = runBlocking { connect.api.getLoginInfo() }.getOrThrow()

        id = info.id
        name = info.nickname

//        friends = DynamicFriendList(this)
        groups = DynamicGroupList(this)

        logger.info("$this 数据初始化完成")
    }

    /**
     * 获取资源
     */
    override fun externalResource(inputStream: InputStream): ExternalResource {
        return OneBotExternalResource(inputStream)
    }

    override suspend fun getMessage(messageId: Int): MessageChain {
        return connect.api.getMsg(messageId).getOrThrow().message
    }

    override fun messageFactory(): MessageFactory {
        return BuildMessageChain()
    }

    override fun toString(): String {
        return "Bot($name[$id])"
    }
}