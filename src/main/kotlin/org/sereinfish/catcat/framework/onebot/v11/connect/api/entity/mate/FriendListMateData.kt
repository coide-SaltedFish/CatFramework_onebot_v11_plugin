package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class FriendListMateData(
    val list: List<FriendInfoMateData>
) {
    companion object: ApiResponseParser<FriendListMateData> {
        private val logger = logger()

        override fun parser(data: JsonElement): FriendListMateData {
            return try {
                val list = ArrayList<FriendInfoMateData>()
                data.asJsonObject["data"].asJsonArray.forEach {
                    list.add(FriendInfoMateData.parser(it))
                }
                FriendListMateData(list)
            }catch (e: Exception) {
                logger.error("解析失败：${data.toJson()}")
                throw e
            }
        }
    }
}