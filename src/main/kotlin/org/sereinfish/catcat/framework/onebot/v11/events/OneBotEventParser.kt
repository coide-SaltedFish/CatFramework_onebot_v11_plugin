package org.sereinfish.catcat.framework.onebot.v11.events

import com.google.gson.JsonElement
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.events.mate.OneBotHeartbeatEvent
import org.sereinfish.catcat.framework.onebot.v11.events.mate.OneBotLifecycleEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.OneBotGroupMessageEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.OneBotPrivateMessageEvent
import org.sereinfish.catcat.framework.onebot.v11.events.notice.*

class OneBotEventParser {
    private val logger = logger()

    private val parsers = HashSet<EventParser>()

    init {
        // 注册事件解析器
        register(OneBotHeartbeatEvent) // 心跳事件
        register(OneBotLifecycleEvent) // 生命周期事件
        // 消息事件
        register(OneBotPrivateMessageEvent) // 私聊消息事件
        register(OneBotGroupMessageEvent) // 群消息事件
        // 通知事件
        register(OneBotGroupMemberDecreaseEvent) // 群成员减少事件
        register(OneBotGroupMemberIncreaseEvent) // 群成员增加事件
        register(OneBotGroupFileUploadEvent) // 群文件上传事件
        register(OneBotGroupAdminSetEvent) // 群管理员变动事件
        register(OneBotGroupAdminUnsetEvent)
        register(OneBotGroupMuteEvent) // 群禁言事件
        register(OneBotFriendAddEvent) // 好友添加事件
        register(OneBotGroupMessageRecallEvent) // 消息撤回事件
        register(OneBotFriendMessageRecallEvent) // 好友消息撤回事件
        register(OneBotGroupPokeEvent) // 群戳一戳事件
    }

    fun register(parser: EventParser) = parsers.add(parser)

    fun parse(data: JsonElement): OneBotEvent {
        parsers.forEach {
            if (it.type.match(data.asJsonObject["post_type"].asString)) {
                if (it.match(data)) {
                    return it.parser(data)
                }
            }
        }
        logger.error("事件解析失败，无法解析该事件：${data.toJson()}")
        error("无法解析的事件数据")
    }
}