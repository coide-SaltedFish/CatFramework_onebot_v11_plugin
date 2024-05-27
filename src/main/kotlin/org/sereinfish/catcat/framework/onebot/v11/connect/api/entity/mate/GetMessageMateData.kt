package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser
import org.sereinfish.catcat.framework.onebot.v11.message.OneBotMessageParser

data class GetMessageMateData(
    val time: Long,
    val messageType: String,
    val messageId: Int,
    val realId: Int,
    val sender: UserInfoMateData,
    val message: MessageChain,
){
    companion object: ApiResponseParser<GetMessageMateData> {
        override fun parser(data: JsonElement): GetMessageMateData {
            val obj = data.asJsonObject["data"].asJsonObject

            val time = obj.get("time").asLong
            val messageType = obj.get("message_type").asString
            val messageId = obj.get("message_id").asInt
            val realId = obj.get("real_id").asInt
            val sender = Gson().fromJson(obj.get("sender"), UserInfoMateData::class.java)
            val message = OneBotMessageParser.parse(obj.get("message"))

            return GetMessageMateData(time, messageType, messageId, realId, sender, message)
        }
    }
}
