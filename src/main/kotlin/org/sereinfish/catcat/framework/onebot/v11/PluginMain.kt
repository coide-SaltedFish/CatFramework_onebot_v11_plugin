package org.sereinfish.catcat.framework.onebot.v11

import org.sereinfish.cat.frame.plugin.Plugin
import org.sereinfish.catcat.framework.onebot.v11.events.OneBotManager

object PluginMain: Plugin {
    override fun start() {
        logger.info("已引入onebot v11实现")

        OneBotManager
    }
}