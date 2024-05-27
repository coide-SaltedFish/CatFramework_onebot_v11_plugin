package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class GroupListInfoMateData(
    val list: List<GroupInfoMateData>
) {
    companion object: ApiResponseParser<GroupListInfoMateData> {
        val logger = logger()

        override fun parser(data: JsonElement): GroupListInfoMateData {
            try {
                val list = ArrayList<GroupInfoMateData>()
                data.asJsonObject["data"].asJsonArray.forEach {
                    list.add(GroupInfoMateData.parser(it))
                }
                return GroupListInfoMateData(list)
            }catch (e: Exception){
                logger.error("数据实例化失败：${data.toJson()}")
                throw e
            }
        }
    }
}