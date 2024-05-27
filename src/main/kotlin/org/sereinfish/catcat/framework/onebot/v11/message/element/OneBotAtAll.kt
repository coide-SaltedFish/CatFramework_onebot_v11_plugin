package org.sereinfish.catcat.framework.onebot.v11.message.element

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.AtAll

class OneBotAtAll: AtAll {
    override val content: String = "[AtAll]"

    override val target: Long = -1L

    override fun encode(): Any {
        return mapOf(
            "type" to OneBotAt.type,
            "data" to mapOf(
                "qq" to "all"
            )
        )
    }
}