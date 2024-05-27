package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class GroupInfoMateData(
    val groupId: Long,
    val groupName: String,
    val memberCount: Int,
    val maxMemberCount: Int
) {
    companion object: ApiResponseParser<GroupInfoMateData> {
        private val logger = logger()

        override fun parser(data: JsonElement): GroupInfoMateData {
            try {
                val obj = if (data.asJsonObject.has("data"))
                    data.asJsonObject["data"].asJsonObject
                else data.asJsonObject

                val groupId = obj["group_id"].asLong
                val groupName = obj["group_name"].asString
                val memberCount = obj["member_count"].asInt
                val maxMemberCount = obj["max_member_count"].asInt

                return GroupInfoMateData(groupId, groupName, memberCount, maxMemberCount)
            }catch (e: Exception){
                logger.error("数据实例化失败：${data.toJson()}")
                throw e
            }
        }
    }
}