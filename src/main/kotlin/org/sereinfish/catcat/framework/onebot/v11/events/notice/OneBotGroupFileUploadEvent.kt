package org.sereinfish.catcat.framework.onebot.v11.events.notice

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Group
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Member
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.notice.GroupFileUploadEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageReceipt
import org.sereinfish.catcat.framework.onebot.v11.events.EventParser
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEvent
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotEventType
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager
import org.sereinfish.catcat.framework.onebot.v11.utils.toUniversalId

class OneBotGroupFileUploadEvent(
    override val bot: Bot,
    override val time: Long,
    override val file: GroupFileUploadEvent.FileInfo,
    override val sender: Member,
    override val group: Group
): GroupFileUploadEvent, OneBotGroupNoticeEvent() {

    internal companion object: EventParser {
        override val type: OneBotEventType = OneBotEventType.NOTICE

        override fun match(data: JsonElement): Boolean {
            val obj = data.asJsonObject
            return obj["notice_type"].asString == "group_upload"
        }

        override fun parser(data: JsonElement): OneBotEvent {
            val obj = data.asJsonObject

            val time = obj["time"].asLong
            val selfId = obj["self_id"].asLong.toUniversalId()
            val groupId = obj["group_id"].asLong.toUniversalId()
            val senderId = obj["user_id"].asLong.toUniversalId()
            val fileInfo = obj["file"].asJsonObject

            val bot = OneBotManager[selfId] ?: error("无法找到对应Bot对象，无法完成事件实例化：$selfId")
            val group = bot.groups[groupId] ?: error("无法找到对应群对象：$groupId")
            val sender = group.members[senderId] ?: error("无法找到对应群成员对象：$senderId")
            val file = FileInfo(fileInfo)

            return OneBotGroupFileUploadEvent(
                bot = bot,
                time = time,
                file = file,
                sender = sender,
                group = group
            )
        }
    }

    class FileInfo(
        override val busid: Long,
        override val id: String,
        override val name: String,
        override val size: Long
    ) : GroupFileUploadEvent.FileInfo {

        constructor(data: JsonObject) : this(
            busid = data["busid"].asLong,
            id = data["id"].asString,
            name = data["name"].asString,
            size = data["size"].asLong
        )
    }

    override fun toLogString(): String {
        return "$bot $group > $sender 上传了一个群文件：${file.name}"
    }
}