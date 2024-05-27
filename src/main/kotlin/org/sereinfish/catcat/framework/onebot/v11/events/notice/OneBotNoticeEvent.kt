package org.sereinfish.catcat.framework.onebot.v11.events.notice

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.NoticeEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import java.util.UUID

abstract class OneBotNoticeEvent(
    override val uuid: String = UUID.randomUUID().toString()
): NoticeEvent, OneBotEvent