package org.sereinfish.catcat.framework.onebot.v11.utils

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalIdAbstract

class OneBotUniversalImageId(
    override val id: String
) : UniversalId, UniversalIdAbstract() {
    override fun encodeToString(): String {
        return id
    }

    override fun toString(): String = id
}