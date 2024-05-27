package org.sereinfish.catcat.framework.onebot.v11.message.element.video

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Video
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotVideo
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap
import org.sereinfish.catcat.framework.onebot.v11.utils.isNonNull
import org.sereinfish.catcat.framework.onebot.v11.utils.toOneBotInt
import java.io.File
import java.io.InputStream
import java.util.*

class OneBotOfflineVideo private constructor(
    override val file: String,
    val isCache: Boolean = true,
    val isProxy: Boolean = true,
    val timeout: Long? = null
) : OneBotVideo() {

    companion object {
        fun buildByFile(
            file: File,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVideo {
            return OneBotOfflineVideo(file = "file:///${file.absolutePath}", isCache, isProxy, timeout)
        }

        fun buildByUrl(
            url: String,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVideo {
            return OneBotOfflineVideo(file = url, isCache, isProxy, timeout)
        }

        fun buildByBase64(
            inputStream: InputStream,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVideo {
            // 将传入的图片文件流编码为base64字符串
            val base64 = Base64.getEncoder().encodeToString(inputStream.readBytes())
            return OneBotOfflineVideo(file = "base64://$base64", isCache, isProxy, timeout)
        }
    }

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "file" to file
                "cache" to isCache.toOneBotInt
                "proxy" to isProxy.toOneBotInt
                timeout.isNonNull {
                    "timeout" to it
                }
            }
        }
    }

    override suspend fun queryUrl(): String {
        error("离线视频无法获取Url")
    }
}