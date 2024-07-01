package org.sereinfish.catcat.framework.onebot.v11.contact.list

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMemberDecreaseEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMemberIncreaseEvent
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.eventhandler.extend.build.buildEventHandler
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotGroup
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotMember
import java.util.concurrent.ConcurrentHashMap

internal class DynamicGroupMemberList(
    val bot: OneBot,
    val group: OneBotGroup,
): ConcurrentHashMap<Long, OneBotMember>() {
    private val logger = logger()

    init {
        val list = OneBotMember.builds(bot, group)

        list.forEach {
            this[it.id] = it
        }

        listener()

        logger.debug("{}[{}]群成员列表初始化完成：{}", bot, group, list.size)
    }

    private fun listener() {
        // 群成员减少
        EventManager.registerHandler(buildEventHandler<GroupMemberDecreaseEvent>(builder = {
            filter {
                event.bot.id == bot.id && event.group.id == group.id
            }
        }) {
            this@DynamicGroupMemberList.remove(user.id)
        })

        // 群成员增加
        EventManager.registerHandler(buildEventHandler<GroupMemberIncreaseEvent>(builder = {
            filter {
                event.bot.id == bot.id && event.group.id == group.id
            }
        }) {
            // 构建群成员信息
            if (this@DynamicGroupMemberList.containsKey(user.id).not()) {
                val newMember = OneBotMember.build(this@DynamicGroupMemberList.bot, group, user.id)
                this@DynamicGroupMemberList[newMember.id] = newMember
            }
        })
    }

    override fun get(key: Long): OneBotMember? {
        return super.get(key) ?: runCatching { OneBotMember.build(bot, group, key) }.getOrNull()?.also {
            this[it.id] = it
        }
    }
}