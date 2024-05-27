package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class EmptyData(
    val status: String,
    val retcode: Int,
    val echo: String,
) {
    companion object: ApiResponseParser<EmptyData> {
        override fun parser(data: JsonElement): EmptyData {
            val obj = data.asJsonObject
            return EmptyData(obj["status"].asString, obj["retcode"].asInt, obj["echo"].asString)
        }
    }

    fun isSuccess(): Boolean = retcode == 0
}