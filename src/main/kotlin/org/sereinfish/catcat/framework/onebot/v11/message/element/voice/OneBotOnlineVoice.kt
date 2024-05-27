package org.sereinfish.catcat.framework.onebot.v11.message.element.voice

import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotVoice
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotOnlineVoice(
    override val file: String,
    val magic: Boolean,
    val url: String
): OneBotVoice() {
    override val content: String = "[Voice:$url]"

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "file" to url
                "magic" to if (magic) 1 else 0
                "cache" to 1
            }
        }
    }

    override suspend fun queryUrl(): String {
        return url
    }

    override fun toLogString(): String {
        return "[语音]"
    }
}