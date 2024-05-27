package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class GroupMemberListInfoMateData(
    val list: List<GroupMemberInfoMateData>
){
    companion object: ApiResponseParser<GroupMemberListInfoMateData> {
        override fun parser(data: JsonElement): GroupMemberListInfoMateData {
            val list = ArrayList<GroupMemberInfoMateData>()
            data.asJsonObject["data"].asJsonArray.forEach {
                list.add(GroupMemberInfoMateData.parser(it))
            }
            return GroupMemberListInfoMateData(list)
        }
    }
}
