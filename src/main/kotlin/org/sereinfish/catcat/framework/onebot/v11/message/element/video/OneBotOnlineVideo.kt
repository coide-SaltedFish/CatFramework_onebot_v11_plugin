package org.sereinfish.catcat.framework.onebot.v11.message.element.video

import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotVideo
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotOnlineVideo(
    override val file: String,
    val url: String
): OneBotVideo() {
    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "file" to url
                "cache" to 1
            }
        }
    }

    override suspend fun queryUrl(): String {
        return url
    }
}