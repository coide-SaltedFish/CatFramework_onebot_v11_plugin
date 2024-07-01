package org.sereinfish.catcat.framework.onebot.v11.message

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotForward
import org.sereinfish.catcat.framework.onebot.v11.message.element.forward.OneBotOfflineForward

internal open class OneBotMessageChain: MessageChain, ArrayList<Message>() {

    internal fun marge(messageChain: MessageChain){
        messageChain.forEach {
            if (it is MessageChain) marge(it)
            else add(it)
        }
    }

    override fun encode(): Any {
        val list = ArrayList<Any>()
        encode(list, this)

        return list
    }

    private fun encode(list: MutableList<Any>, messageChain: MessageChain){
        messageChain.forEach {
            if (it is MessageChain) encode(list, it)
            if (it is OneBotOfflineForward) {
                it.nodes.forEach {
                    list.add(it.encode())
                }

            }
            else list.add(it.encode())
        }
    }

    override fun serializeToJsonString(): String {
        return encode().toJson()
    }
}