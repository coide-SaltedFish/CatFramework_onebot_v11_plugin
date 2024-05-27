package org.sereinfish.catcat.framework.onebot.v11.connect

import org.sereinfish.catcat.framework.onebot.v11.connect.api.OneBotApi
import org.sereinfish.catcat.framework.onebot.v11.connect.request.OneBotRequestData

interface OneBotConnect {

    /**
     * 接口实现
     */
    val api: OneBotApi

    fun connect(): Boolean

    fun close()

    /**
     * 数据解析
     */
    fun response(data: String)

    /**
     * 请求数据
     */
    fun execute(request: OneBotRequestData)
}