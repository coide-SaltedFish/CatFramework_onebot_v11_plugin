package org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser

import com.google.gson.JsonElement

internal interface ApiResponseParser<T> {
    fun parser(data: JsonElement): T
}