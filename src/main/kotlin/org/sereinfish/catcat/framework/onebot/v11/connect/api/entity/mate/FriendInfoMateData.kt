package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class FriendInfoMateData(
    val userId: Long,
    val nickname: String,
    val remark: String,
){
    companion object: ApiResponseParser<FriendInfoMateData> {
        private val logger = logger()

        override fun parser(data: JsonElement): FriendInfoMateData {
            return try {
                val obj = data.asJsonObject

                val userId = obj["user_id"].asLong
                val nickname = obj["nickname"].asString
                val remark = obj["remark"].asString

                FriendInfoMateData(userId, nickname, remark)
            }catch (e: Exception) {
                logger.error("解析失败：${data.toJson()}")
                throw e
            }
        }
    }
}
