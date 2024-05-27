package org.sereinfish.catcat.framework.onebot.v11.message.element.forward

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotForward

class OneBotOfflineForward(
    override val nodes: List<OneBotOfflineNode>,
) : OneBotForward() {

    override fun encode(): Any {
        return nodes.map {
            it.encode()
        }
    }

    class OneBotOfflineNode(
        override val message: Message,
        override val userId: Long,
        override val nickname: String
    ) : OneBotNode
}