package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class UserInfoMateData(
    val userId: Long,
    val nickname: String,
    val sex: String,
    val age: Int,
){
    companion object: ApiResponseParser<UserInfoMateData> {
        override fun parser(data: JsonElement): UserInfoMateData {
            val obj = data.asJsonObject["data"].asJsonObject

            val userId = obj["user_id"].asLong
            val nickname = obj["nickname"].asString
            val sex = obj["sex"].asString
            val age = obj["age"].asInt

            return UserInfoMateData(userId, nickname, sex, age)
        }
    }
}
