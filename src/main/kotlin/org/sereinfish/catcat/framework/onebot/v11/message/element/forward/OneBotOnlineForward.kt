package org.sereinfish.catcat.framework.onebot.v11.message.element.forward

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotForward
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotOnlineForward(
    val id: String
) : OneBotForward() {
    override val nodes: List<OneBotOnlineNode> by lazy { runBlocking { getForwardMessage() } }

    private suspend fun getForwardMessage(): List<OneBotOnlineNode> {
        val bot = OneBotManager.list().firstOrNull() ?: error("尚未登录任何Bot，无法解析消息")
        val data = bot.connect.api.getForwardMsg(id).getOrThrow()

        return data.messages.map {
            val message = OneBotMessageParser.parse(it.message)
            OneBotOnlineNode(message, it.sender.userId.toUniversalId(), it.sender.nickname)
        }
    }

    class OneBotOnlineNode(
        override val message: MessageChain,
        override val userId: UniversalId,
        override val nickname: String
    ) : OneBotNode
}