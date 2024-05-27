package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser

data class ForwardMsgInfoMateData(
    val messages: List<Message>,
){
    companion object: ApiResponseParser<ForwardMsgInfoMateData> {
        override fun parser(data: JsonElement): ForwardMsgInfoMateData {
            val list = mutableListOf<Message>()

            val obj = data.asJsonObject["data"].asJsonObject

            obj["messages"].asJsonArray.forEach {
                list.add(Message.parser(it))
            }

            return ForwardMsgInfoMateData(list)
        }
    }

    data class Message(
        val time: Long,
        val messageType: String,
        val messageId: Int,
        val realId: Int,
        val sender: UserInfoMateData,
        val message: List<MessageItemMateData>,
        val peerId: Long,
    ){
        companion object: ApiResponseParser<Message> {
            override fun parser(data: JsonElement): Message {
                val obj = data.asJsonObject

                return Message(
                    obj["time"].asLong,
                    obj["message_type"].asString,
                    obj["message_id"].asInt,
                    obj["real_id"].asInt,
                    UserInfoMateData.parser(obj["sender"]),
                    obj["message"].asJsonArray.map { MessageItemMateData.parser(it) },
                    obj["peer_id"].asLong
                )
            }
        }
    }
}