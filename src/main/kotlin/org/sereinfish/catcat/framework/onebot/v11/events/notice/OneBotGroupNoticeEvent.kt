package org.sereinfish.catcat.framework.onebot.v11.events.notice

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupNoticeEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt

abstract class OneBotGroupNoticeEvent: GroupNoticeEvent, OneBotNoticeEvent() {
    override suspend fun sendMessage(message: Message): MessageReceipt {
        return group.sendMessage(message)
    }
}