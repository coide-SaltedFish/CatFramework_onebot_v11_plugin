package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

/**
 * 登录号信息
 */
data class LoginBotInfoMateData(
    val id: Long,
    val nickname: String,
){
    companion object: ApiResponseParser<LoginBotInfoMateData> {
        override fun parser(data: JsonElement): LoginBotInfoMateData {
            val obj = data.asJsonObject["data"].asJsonObject
            return LoginBotInfoMateData(
                obj["user_id"].asLong,
                obj["nickname"].asString
            )
        }
    }
}
