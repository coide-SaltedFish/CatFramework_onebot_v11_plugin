package org.sereinfish.catcat.framework.onebot.v11.events.mate

import com.google.gson.JsonElement
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate.GetStatusInfoMateData
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType

/**
 * 心跳事件
 */
class OneBotHeartbeatEvent(
    val time: Long,
    val selfId: Long,
    val status: GetStatusInfoMateData,
    val interval: Long,
) : OneBotMateEvent() {

    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.MATE_EVENT

        override fun match(data: JsonElement): Boolean {
            return data.asJsonObject["meta_event_type"].asString == "heartbeat"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time: Long = obj["time"].asLong
            val selfId: Long = obj["self_id"].asLong
            val status: GetStatusInfoMateData = GetStatusInfoMateData.parser(obj["status"])
            val interval: Long = obj["interval"].asLong

            return OneBotHeartbeatEvent(time, selfId, status, interval)
        }
    }

    override fun toLogString(): String {
        return "OneBotHeartbeatEvent(online=${status.online}, good=${status.good}, interval=$interval)"
    }
}