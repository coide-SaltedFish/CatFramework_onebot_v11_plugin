package org.sereinfish.catcat.framework.onebot.v11.message

/**
 * 通用的多媒体消息接口
 */
interface MultiMediaMessage {
    val file: String

    suspend fun queryUrl(): String
}