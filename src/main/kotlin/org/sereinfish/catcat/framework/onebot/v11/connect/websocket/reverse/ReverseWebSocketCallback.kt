package org.sereinfish.catcat.framework.onebot.v11.connect.websocket.reverse

import com.google.gson.JsonElement

internal interface ReverseWebSocketCallback{
    val uuid: String
    var cancel: Boolean

    fun callback(data: JsonElement)
}
