package org.sereinfish.catcat.framework.onebot.v11.message

import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.ExternalResource
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Image
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Video
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.element.Voice
import org.sereinfish.catcat.framework.onebot.v11.message.element.image.OneBotOfflineImage
import org.sereinfish.catcat.framework.onebot.v11.message.element.video.OneBotOfflineVideo
import org.sereinfish.catcat.framework.onebot.v11.message.element.voice.OneBotOfflineVoice
import java.io.File
import java.io.InputStream

class OneBotExternalResource(
    override val inputStream: InputStream,
    override var autoCloseable: Boolean = true
): ExternalResource {
    override suspend fun uploadAsImage(): Image {
        return inputStream.use {
            OneBotOfflineImage.buildByBase64(it)
        }
    }

    override suspend fun uploadAsVideo(): Video {
        return inputStream.use {
            OneBotOfflineVideo.buildByBase64(it)
        }
    }

    override suspend fun uploadAsVoice(): Voice {
        return inputStream.use {
            OneBotOfflineVoice.buildByBase64(it)
        }
    }
}


class OneBotFileExternalResource(
    val file: File
): ExternalResource {
    override var autoCloseable: Boolean = true
    override val inputStream: InputStream get() = file.inputStream()


    override suspend fun uploadAsImage(): Image {
        return OneBotOfflineImage.buildByFile(file)
    }

    override suspend fun uploadAsVideo(): Video {
        return OneBotOfflineVideo.buildByFile(file)
    }

    override suspend fun uploadAsVoice(): Voice {
        return OneBotOfflineVoice.buildByFile(file)
    }
}