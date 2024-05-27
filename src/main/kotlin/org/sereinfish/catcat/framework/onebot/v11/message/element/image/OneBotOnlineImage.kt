package org.sereinfish.catcat.framework.onebot.v11.message.element.image

import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotImage
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotOnlineImage(
    override val file: String,
    val imageType: String,
    val url: String
): OneBotImage() {
    override val id: String = file.uppercase()

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "file" to url
            }
        }
    }

    override suspend fun queryUrl(): String {
        return url
    }
}