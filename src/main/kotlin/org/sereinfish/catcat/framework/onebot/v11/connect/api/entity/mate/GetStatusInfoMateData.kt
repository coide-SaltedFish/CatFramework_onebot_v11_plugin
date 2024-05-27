package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

/**
 * online	boolean	当前 QQ 在线，null 表示无法查询到在线状态
 * good	boolean	状态符合预期，意味着各模块正常运行、功能正常，且 QQ 在线
 */
data class GetStatusInfoMateData(
    val online: Boolean,
    val good: Boolean,
    val data: Map<*, *>,
) {
    companion object: ApiResponseParser<GetStatusInfoMateData> {
        override fun parser(data: JsonElement): GetStatusInfoMateData {
            val obj = data.asJsonObject
            return GetStatusInfoMateData(
                obj["online"].asBoolean,
                obj["good"].asBoolean,
                data = Gson().fromJson(data, Map::class.java)
            )
        }
    }
}