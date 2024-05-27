package org.sereinfish.catcat.framework.onebot.v11.events

enum class OneBotEventType(
    val value: String
) {
    MESSAGE("message"),
    NOTICE("notice"),
    REQUEST("request"),
    MATE_EVENT("meta_event")
    ;
    companion object {
        fun parser(value: String): OneBotEventType {
            return entries.find { it.value == value } ?: error("未知的事件类型：$value")
        }
    }

    fun match(value: String): Boolean {
        return value == this.value
    }
}