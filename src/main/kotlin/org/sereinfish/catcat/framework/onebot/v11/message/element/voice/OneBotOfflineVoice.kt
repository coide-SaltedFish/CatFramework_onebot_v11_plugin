package org.sereinfish.catcat.framework.onebot.v11.message.element.voice

import org.sereinfish.catcat.framework.onebot.v11.message.element.OneBotVoice
import org.sereinfish.catcat.framework.onebot.v11.utils.buildCatMap
import org.sereinfish.catcat.framework.onebot.v11.utils.isNonNull
import org.sereinfish.catcat.framework.onebot.v11.utils.toOneBotInt
import java.io.File
import java.io.InputStream
import java.util.*

class OneBotOfflineVoice private constructor(
    override val file: String,
    val isCache: Boolean = true,
    val isProxy: Boolean = true,
    val timeout: Long? = null
) : OneBotVoice() {

    companion object {
        fun buildByFile(
            file: File,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVoice {
            return OneBotOfflineVoice(file = "file:///${file.absolutePath}", isCache, isProxy, timeout)
        }

        fun buildByUrl(
            url: String,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVoice {
            return OneBotOfflineVoice(file = url, isCache, isProxy, timeout)
        }

        fun buildByBase64(
            inputStream: InputStream,
            isCache: Boolean = true,
            isProxy: Boolean = true,
            timeout: Long? = null
        ): OneBotOfflineVoice {
            // 将传入的图片文件流编码为base64字符串
            val base64 = Base64.getEncoder().encodeToString(inputStream.readBytes())
            return OneBotOfflineVoice(file = "base64://$base64", isCache, isProxy, timeout)
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
        error("离线语音无法获取Url")
    }
}