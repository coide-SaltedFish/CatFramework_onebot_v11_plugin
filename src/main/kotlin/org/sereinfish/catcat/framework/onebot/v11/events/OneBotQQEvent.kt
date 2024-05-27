package org.sereinfish.catcat.framework.onebot.v11.events

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.QQEvent
import java.util.UUID

abstract class OneBotQQEvent(
    override val bot: Bot,
    override val time: Long,
    override val uuid: String = UUID.randomUUID().toString()
): QQEvent, OneBotEvent