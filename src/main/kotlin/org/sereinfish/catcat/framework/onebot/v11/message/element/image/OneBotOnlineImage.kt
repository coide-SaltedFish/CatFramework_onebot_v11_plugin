package org.sereinfish.catcat.framework.onebot.v11.message.element.image

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotImage
import org.sereinfish.catcat.framework.onebot.v11.utils.OneBotUniversalImageId
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap

class OneBotOnlineImage(
    override val file: String,
    val imageType: String,
    val url: String
): OneBotImage() {
    override val id: UniversalId = OneBotUniversalImageId(file.uppercase())

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