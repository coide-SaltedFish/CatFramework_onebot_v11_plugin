package org.sereinfish.catcat.framework.onebot.v11.message

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Forward
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.forward.ForwardMessageFactory
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.message.element.forward.OneBotOfflineForward

class OneBotForwardMessageFactory: ForwardMessageFactory {
    override val nodes: MutableList<Forward.Node> = ArrayList()

    override fun build(): Forward {
        return OneBotOfflineForward(nodes.map { it as OneBotOfflineForward.OneBotOfflineNode })
    }

    override fun node(userId: UniversalId, name: String, message: Message): Forward.Node {
        return OneBotOfflineForward.OneBotOfflineNode(message, userId, name)
    }
}