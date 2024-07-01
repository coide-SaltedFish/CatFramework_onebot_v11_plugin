package org.sereinfish.catcat.framework.onebot.v11.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.contact.Contact
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.events.message.send.MessageSendingEvent
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.MessageChain
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate.ForwardMsgInfoMateData
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.message.buildMessageChain
import kotlin.experimental.ExperimentalTypeInference

class MapBuilder<K ,V> {
    private val map = mutableMapOf<K, V>()

    infix fun K.to(value: V) {
        map[this] = value
    }

    fun build(): Map<K, V> = map
}

fun <T> Boolean.isTrue(block: Boolean.() -> T): T? {
    return if (this) block() else null
}

fun <T> Boolean.isFlase(block: Boolean.() -> T): T? {
    return if (this.not()) block() else null
}

@OptIn(ExperimentalTypeInference::class)
fun <T: Any, R> T?.isNonNull(@BuilderInference block: (T) -> R): R? {
    return this?.let {
        block(it)
    }
}

val Boolean.toOneBotInt get() = if (this) 1 else 0

@OptIn(ExperimentalTypeInference::class)
fun <K, V> buildCatMap(@BuilderInference builder: MapBuilder<K, V>.() -> Unit): Map<K, V> {
    return MapBuilder<K, V>().apply(builder).build()
}

val JsonElement.asStringOrNull: String? get() =
    if (this.isJsonPrimitive)
        if (this.asJsonPrimitive.isString)
            this.asString
        else null
    else null

val JsonElement.asIntOrNull: Int? get() =
    if (this.isJsonPrimitive)
        if (this.asJsonPrimitive.isNumber)
            this.asInt
        else null
    else null

val JsonElement.asJsonObjectOrNUll: JsonObject? get() =
    if (this.isJsonObject)
        this.asJsonObject
    else null