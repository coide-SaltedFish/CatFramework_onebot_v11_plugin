package org.sereinfish.catcat.framework.onebot.v11.message.element.image

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotImage
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap
import org.sereinfish.catcat.framework.onebot.v11.utils.isNonNull
import org.sereinfish.catcat.framework.onebot.v11.utils.isTrue
import org.sereinfish.catcat.framework.onebot.v11.utils.toOneBotInt
import java.io.File
import java.io.InputStream
import java.util.Base64

class OneBotOfflineImage private constructor(
    override val file: String,
    val isFlash: Boolean = false,
    val isCache: Boolean = true,
    val isProxy: Boolean = true,
    val timeout: Long? = null
): OneBotImage(){
    override val id: UniversalId get() = error("离线图片尚未分配ID")

    companion object {
        private val logger = logger()

        fun buildByFile(
            file: File,
            isFlash: Boolean = false,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineImage {
            return OneBotOfflineImage(file = "file:///${file.absolutePath}", isFlash, isCache, isProxy, timeout)
        }

        fun buildByUrl(
            url: String,
            isFlash: Boolean = false,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineImage {
            return OneBotOfflineImage(file = url, isFlash, isCache, isProxy, timeout)
        }

        fun buildByBase64(
            inputStream: InputStream,
            isFlash: Boolean = false,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineImage {
            // 将传入的图片文件流编码为 base64 字符串
            val bytes = inputStream.readBytes()
            logger.info("image bytes size = ${bytes.size}")
            val base64 = Base64.getEncoder().encodeToString(bytes)
            return OneBotOfflineImage(file = "base64://$base64", isFlash, isCache, isProxy, timeout)
        }
    }

    override fun encode(): Any {
        return buildCatMap {
            "type" to type
            "data" to buildCatMap {
                "file" to file
                isFlash.isTrue { "type" to "flash" }
                "cache" to isCache.toOneBotInt
                "proxy" to isProxy.toOneBotInt
                timeout.isNonNull {
                    "timeout" to it
                }
            }
        }
    }

    override suspend fun queryUrl(): String {
        error("离线图片无法获取Url")
    }
}