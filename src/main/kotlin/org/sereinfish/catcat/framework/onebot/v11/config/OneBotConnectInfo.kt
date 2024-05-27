package org.sereinfish.catcat.framework.onebot.v11.config

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

/**
 * 连接配置信息
 *
 * @param type 连接类型 HTTP WebSocket ReverseWebSocket
 */
data class OneBotConnectInfo(
    val type: ConnectType,
    val host: String,
    val port: Int,
    val interval: Long = 3000,
    val accessToken: String? = null
){
    enum class ConnectType {
        HTTP,
        WebSocket,
        ReverseWebSocket
    }

    fun connect(): Bot {
        return when(type){
            ConnectType.HTTP -> {
                TODO()
            }
            ConnectType.WebSocket -> TODO()
            ConnectType.ReverseWebSocket -> {
                OneBotManager.loginByRws("ws://$host:$port")
            }
        }
    }

    override fun toString(): String {
        return "OneBotConnectInfo(type=$type, host='$host', port=$port, interval=$interval, accessToken=$accessToken)"
    }
}
