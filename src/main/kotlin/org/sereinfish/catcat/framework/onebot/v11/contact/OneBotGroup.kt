package org.sereinfish.catcat.framework.onebot.v11.contact

import kotlinx.coroutines.runBlocking
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.contact.list.DynamicGroupMemberList
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotGroupMessageSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotGroupMessageSentEvent

class OneBotGroup private constructor(
    override val bot: OneBot,
    override val id: Long,
    override var groupName: String
): Group {

    override val members: Map<Long, Member> = DynamicGroupMemberList(bot, this)
    override val name: String = groupName

    companion object {
        internal fun build(bot: OneBot, id: Long): OneBotGroup {
            val data = runBlocking { bot.connect.api.getGroupInfo(id) }.getOrThrow()
            return OneBotGroup(bot, data.groupId, data.groupName)
        }

        internal fun builds(bot: OneBot): List<OneBotGroup> {
            val data = runBlocking { bot.connect.api.getGroupList() }.getOrThrow()
            return data.list.map {
                OneBotGroup(bot, it.groupId, it.groupName)
            }
        }
    }

    override suspend fun admin(id: Long, set: Boolean): Boolean {
        return bot.connect.api.setGroupAdmin(this.id, id, set)
    }

    override suspend fun allMute(flag: Boolean): Boolean {
        return bot.connect.api.groupWholeBan(this.id, flag)
    }

    override suspend fun kick(id: Long, allowRejoinAfterKickout: Boolean): Boolean {
        return bot.connect.api.groupKick(this.id, id, allowRejoinAfterKickout)
    }

    override suspend fun leave(isDismiss: Boolean): Boolean {
        return bot.connect.api.groupLeave(this.id, isDismiss)
    }

    override suspend fun mute(id: Long, time: Int): Boolean {
        return bot.connect.api.groupBan(this.id, id, time.toLong())
    }

    override suspend fun sendMessage(message: Message): MessageReceipt {
        EventManager.broadcast(OneBotGroupMessageSendingEvent(bot, message, this, bot))
        return bot.connect.api.sendGroupMsg(this.id, message).getOrThrow().parser(bot, message).also {
            // 获取消息
            val retMessageInfo = bot.connect.api.getMsg(it.messageId).getOrThrow()
            val onlineMessage = bot.messageParser.parserOnline(bot, this, bot, it.messageId, retMessageInfo.message)

            EventManager.broadcast(OneBotGroupMessageSentEvent(bot, this, bot, onlineMessage))
        }
    }

    override fun toString(): String {
        return "Group($name[$id])"
    }
}