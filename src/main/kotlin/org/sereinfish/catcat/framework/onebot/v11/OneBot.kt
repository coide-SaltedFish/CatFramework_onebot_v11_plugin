package org.sereinfish.catcat.framework.onebot.v11

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.ExternalResource
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageFactory
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.connect.OneBotConnect
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotUser
import org.sereinfish.catcat.framework.onebot.v11.contact.list.DynamicFriendList
import org.sereinfish.catcat.framework.onebot.v11.contact.list.DynamicGroupList
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventParser
import org.sereinfish.catcat.framework.onebot.v11.message.BuildMessageChain
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotExternalResource
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotFileExternalResource
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalId
import java.io.File
import java.io.InputStream

class OneBot(
    val connect: OneBotConnect
): Bot {
    private val logger = logger()
    override val bot: Bot = this

    override val friends: Map<UniversalId, Friend>
    override val groups: Map<UniversalId, Group>
    override val id: OneBotUniversalId
    override val name: String
    override val nickname: String

    val eventParser = OneBotEventParser()
    val messageParser = OneBotMessageParser()

    override val version: String = "OneBot_V11"
    override fun decodeContactId(contactId: String): UniversalId {
        return OneBotUniversalId.decode(contactId)
    }

    override fun deserializeFromJson(json: String): MessageChain {
        return messageParser.parse(JsonParser.parseString(json))
    }

    private val client = OkHttpClient.Builder()
        .build()

    init {
        // 获取基本信息
        logger.info("$this 正在初始化数据")

        val info = runBlocking { connect.api.getLoginInfo() }.getOrThrow()

        id = OneBotUniversalId(info.id)
        name = info.nickname
        nickname = name

        friends = DynamicFriendList(this)
        groups = DynamicGroupList(this)

        logger.info("$this 数据初始化完成")
    }

    /**
     * 获取资源
     */
    override fun externalResource(inputStream: InputStream): ExternalResource {
        return OneBotExternalResource(inputStream)
    }

    override fun externalResource(file: File): ExternalResource {
        return OneBotFileExternalResource(file)
    }

    override suspend fun getMessage(messageId: UniversalId): MessageChain {
        return connect.api.getMsg(messageId).getOrThrow().message
    }

    override fun messageFactory(): MessageFactory {
        return BuildMessageChain()
    }

    override fun queryFaceImage(): ByteArray {
        val request = Request.Builder()
            .url("http://q.qlogo.cn/headimg_dl?dst_uin=$id&spec=640&img_type=jpg")
            .build()
        return OneBotUser.client.newCall(request).execute().body?.use { it.bytes() } ?: error("获取头像失败")
    }

    override fun toString(): String {
        return "Bot($name[$id])"
    }
}