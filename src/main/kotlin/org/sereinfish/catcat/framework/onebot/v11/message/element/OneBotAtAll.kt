package org.sereinfish.catcat.framework.onebot.v11.message.element

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.AtAll
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalId

class OneBotAtAll: AtAll {
    override val content: String = "[AtAll]"

    override val target: UniversalId = OneBotUniversalId(-1L)

    override fun encode(): Any {
        return mapOf(
            "type" to OneBotAt.type,
            "data" to mapOf(
                "qq" to "all"
            )
        )
    }
}