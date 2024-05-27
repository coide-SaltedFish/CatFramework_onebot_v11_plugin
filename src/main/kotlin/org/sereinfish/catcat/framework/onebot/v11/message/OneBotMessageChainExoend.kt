package org.sereinfish.catcat.framework.onebot.v11.message

import com.google.gson.JsonParser
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageFactory
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.*
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.forward.ForwardMessageFactory
import org.sereinfish.cat.frame.utils.toClass
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotAt
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotAtAll
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotFace
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotPlantText

class BuildMessageChain: MessageFactory {
    private val chain = OneBotMessageChain()

    override fun add(message: Message): MessageFactory {
        chain.add(message)
        return this
    }

    override fun at(target: Long): At {
        return OneBotAt(target)
    }

    override fun atAll(): AtAll {
        return OneBotAtAll()
    }

    override fun build(): MessageChain {
        return chain
    }

    override fun deserializeFromJsonString(data: String): MessageChain {
        return OneBotMessageParser.parse(JsonParser.parseString(data))
    }

    override fun face(id: Int): Face {
        return OneBotFace(id)
    }

    override fun forward(): ForwardMessageFactory {
        return OneBotForwardMessageFactory()
    }

    override fun reply(messageId: Int): Reply {
        return OneBotReply(messageId)
    }

    override fun text(text: String): PlantText {
        return OneBotPlantText(text)
    }
}

internal inline fun buildMessageChain(builder: BuildMessageChain.() -> Unit): MessageChain {
    return BuildMessageChain().apply(builder).build()
}