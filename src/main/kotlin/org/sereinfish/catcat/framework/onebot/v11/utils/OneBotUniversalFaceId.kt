package org.sereinfish.catcat.framework.onebot.v11.utils

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId

class OneBotUniversalFaceId(
    override val id: Int
) : UniversalId {
    companion object {
        fun decode(id: String): UniversalId {
            return OneBotUniversalMessageId(id.toIntOrNull() ?: error("错误的Id格式，无法序列化为OneBotUniversalMessageId：$id"))
        }
    }

    override fun encodeToString(): String {
        return "$id"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is OneBotUniversalMessageId)
            id == other.id
        else
            id == other
    }

    override fun toString(): String {
        return "$id"
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}