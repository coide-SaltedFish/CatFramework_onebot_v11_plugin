package org.sereinfish.catcat.framework.onebot.v11.connect.request

import org.sereinfish.cat.frame.utils.toJson
import java.util.*

data class OneBotRequestData(
    val action: String,
    val params: Map<String, Any> = mapOf(),
    val echo: String = UUID.randomUUID().toString()
){
    fun websocket() = this.toJson()
}
