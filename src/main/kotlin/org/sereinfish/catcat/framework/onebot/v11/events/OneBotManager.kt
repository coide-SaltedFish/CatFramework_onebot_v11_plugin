package org.sereinfish.catcat.framework.onebot.v11.events

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.Bot
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.BotManager
import org.sereinfish.cat.frame.config.getClassOrNull
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.PluginMain
import org.sereinfish.catcat.framework.onebot.v11.config.OneBotConnectInfo
import org.sereinfish.catcat.framework.onebot.v11.connect.websocket.reverse.OneBotReverseWebSocket
import org.sereinfish.catcat.framework.onebot.v11.utils.isTrue

/**
 * onebot协议实现的bot对象管理器
 */
object OneBotManager {
    private val logger = logger()
    private val botMap = HashMap<Long, OneBot>()

    operator fun get(id: Long) = botMap[id]

    init {
        loadConfigBot() // 加载已配置的Bot
    }

    private fun loadConfigBot() {
        val bots: List<OneBotConnectInfo> = PluginMain.config.getClassOrNull<JsonElement>("bots")?.let {
            Gson().fromJson(it, object : TypeToken<List<OneBotConnectInfo>>(){}.type)
        } ?: listOf()

        bots.forEach { botInfo ->
            // 构建连接
            val bot = botInfo.connect()
            BotManager.register(bot) // 注册到通用接口层
            logger.info("已通过[${botInfo.type.name}]连接到Bot: ${bot.name}[${bot.id}]")
        }
    }

    fun list(): List<OneBot> = botMap.values.toList()

    fun addBot(bot: OneBot) {
        botMap[bot.id] = bot
    }

    /**
     * 反向WebSocket方式登录 Bot
     */
    fun loginByRws(wsUrl: String): Bot {
        logger.info("尝试通过[ReverseWebSocket]连接到Bot：$wsUrl")

        val connect = OneBotReverseWebSocket(wsUrl).apply {
            connect()
        }
        val bot = OneBot(connect)
        connect.bot = bot

        addBot(bot)
        return bot
    }
}