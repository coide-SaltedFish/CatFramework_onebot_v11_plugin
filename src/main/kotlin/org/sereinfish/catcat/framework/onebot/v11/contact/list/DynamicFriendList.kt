package org.sereinfish.catcat.framework.onebot.v11.contact.list

import kotlinx.coroutines.launch
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Friend
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.cat.frame.utils.creatContextScope
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.eventhandler.extend.build.buildEventHandler
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotFriend
import org.sereinfish.catcat.framework.onebot.v11.events.notice.OneBotFriendAddEvent
import java.util.concurrent.ConcurrentHashMap

class DynamicFriendList(
    val bot: OneBot
): ConcurrentHashMap<UniversalId, Friend>() {
    private val logger = logger()
    private val scope = creatContextScope()

    init {
        // 初始化列表
        putAll(OneBotFriend.builds(bot).associateBy { it.id })

        listener()

        logger.info("好友列表初始化完成：${size}")
    }

    private fun listener() {
        EventManager.registerHandler(buildEventHandler<OneBotFriendAddEvent>(builder = {
            filter { event.bot.id == bot.id }
        }){
            scope.launch {
                putAll(OneBotFriend.builds(this@DynamicFriendList.bot).associateBy { it.id })
            }
        })
    }
}