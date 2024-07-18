package org.sereinfish.catcat.framework.onebot.v11.contact.list

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMemberDecreaseEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupMemberIncreaseEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.eventhandler.extend.build.buildEventHandler
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.contact.OneBotGroup
import java.util.concurrent.ConcurrentHashMap

/**
 * 动态群列表
 */
internal class DynamicGroupList(
    val bot: OneBot
): ConcurrentHashMap<UniversalId, Group>() {
    private val logger = logger()

    init {
        // 初始化列表
        val groups = OneBotGroup.builds(bot)
        groups.forEach {
            this[it.id] = it
        }
        // 注册监听器
        listener()

        logger.info("$bot 群列表初始化完成：${groups.size}")
    }

    private fun listener() {
        // 退出群聊
        EventManager.registerHandler(buildEventHandler<GroupMemberDecreaseEvent>(builder = {
            filter {
                event.user.id == bot.id
            }
        }) {
            this@DynamicGroupList.remove(group.id)
        })
        // 加入群聊
        EventManager.registerHandler(buildEventHandler<GroupMemberIncreaseEvent>(builder = {
            filter { event.user.id == bot.id }
        }) {
            // 构建群信息
            if (this@DynamicGroupList.containsKey(group.id).not()) {
                val newGroup = OneBotGroup.build(this@DynamicGroupList.bot, group.id)
                this@DynamicGroupList[newGroup.id] = newGroup
            }
        })
    }

    override fun get(key: UniversalId): Group? {
        return super.get(key) ?: runCatching {
            OneBotGroup.build(bot, key)
        }.getOrNull()?.also {
            this[it.id] = it
        }
    }
}