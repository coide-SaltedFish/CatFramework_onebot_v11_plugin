package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

/**
 * 消息段
 */
data class MessageItemMateData(
    val type: String,
    val data: JsonElement
){
    companion object: ApiResponseParser<MessageItemMateData> {
        override fun parser(data: JsonElement): MessageItemMateData {
            val obj = data.asJsonObject

            val type = obj["type"].asString
            return MessageItemMateData(type, obj["data"])
        }
    }
}
