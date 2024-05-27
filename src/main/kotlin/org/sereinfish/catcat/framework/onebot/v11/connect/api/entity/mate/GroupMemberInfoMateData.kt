package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class GroupMemberInfoMateData(
    val groupId: Long,
    val userId: Long,
    val nickname: String,
    val cardName: String,
    val sex: String,
    val age: Int,
    val area: String,
    val joinTime: Int,
    val lastSentTime: Int,
    val level: String,
    val role: String,
    val unfriendly: Boolean,
    val title: String,
    val titleExpireTime: Int,
    val cardChangeable: Boolean,
){
    companion object: ApiResponseParser<GroupMemberInfoMateData> {
        override fun parser(data: JsonElement): GroupMemberInfoMateData {
            val obj = if (data.asJsonObject.has("data"))
                data.asJsonObject["data"].asJsonObject
            else
                data.asJsonObject

            return GroupMemberInfoMateData(
                obj["group_id"].asLong,
                obj["user_id"].asLong,
                obj["nickname"].asString,
                obj["card"].asString,
                obj["sex"].asString,
                obj["age"].asInt,
                obj["area"].asString,
                obj["join_time"].asInt,
                obj["last_sent_time"].asInt,
                obj["level"].asString,
                obj["role"].asString,
                obj["unfriendly"].asBoolean,
                obj["title"].asString,
                obj["title_expire_time"].asInt,
                obj["card_changeable"].asBoolean,
            )
        }
    }
}
