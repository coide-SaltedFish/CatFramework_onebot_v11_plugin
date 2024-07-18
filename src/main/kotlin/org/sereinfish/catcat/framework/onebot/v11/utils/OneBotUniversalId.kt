package org.sereinfish.catcat.framework.onebot.v11.utils

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalIdAbstract

class OneBotUniversalId(
    override val id: Long
) : UniversalIdAbstract() {

    companion object {
        fun decode(id: String): UniversalId {
            return OneBotUniversalId(id.toLongOrNull() ?: error("错误的Id格式，无法序列化为OneBotUniversalId：$id"))
        }
    }

    override fun encodeToString(): String {
        return "$id"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is OneBotUniversalId)
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