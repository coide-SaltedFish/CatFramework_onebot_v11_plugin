package org.sereinfish.catcat.framework.onebot.v11.connect.websocket.reverse

import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.nonNull
import org.sereinfish.catcat.framework.onebot.v11.OneBot
import org.sereinfish.catcat.framework.onebot.v11.connect.OneBotConnect
import org.sereinfish.catcat.framework.onebot.v11.connect.api.OneBotApi
import org.sereinfish.catcat.framework.onebot.v11.connect.request.OneBotRequestData
import org.sereinfish.catcat.framework.onebot.v11.connect.websocket.OneBotWebSocket

/**
 * 反向连接
 */
internal class OneBotReverseWebSocket(
    private val wsUrl: String
): OneBotConnect {
    private val logger = logger()
    private val client: OkHttpClient

    // 事件接收器
    private val eventReceiverWebSocket: OneBotWebSocket
    private val apiWebSocket: OneBotWebSocket

    // 创建回调监听列表
    private val callbacks = HashMap<String, ReverseWebSocketCallback>()

    override val api: OneBotApi

    lateinit var bot: OneBot

    init {
        client = OkHttpClient.Builder()
            .build()
        eventReceiverWebSocket = OneBotWebSocket(client, wsUrl) {
            // 接收到事件
            try {
                if (::bot.isInitialized)
                    EventManager.broadcast(bot.eventParser.parse(JsonParser.parseString(it)))
                else
                    logger.debug("Bot实例对象尚未完成初始化，无法进行事件数据解析：$it")
            }catch (e: Exception) {
                logger.error("OneBot事件广播时出现异常", e)
            }
        }
        apiWebSocket = OneBotWebSocket(client, "$wsUrl/api", this::response)

        api = OneBotReverseWebSocketApiImpl().apply {
            webSocket = this@OneBotReverseWebSocket
        }
    }

    override fun connect(): Boolean {
        // 初始化连接
        eventReceiverWebSocket.connect()
        apiWebSocket.connect()
        logger.info("连接成功：$wsUrl")
        return true
    }

    override fun close() {
        apiWebSocket.close()
        eventReceiverWebSocket.close()
    }

    fun addCallback(callback: ReverseWebSocketCallback): Boolean {
        return callbacks.put(callback.uuid, callback).nonNull()
    }

    /**
     * 进行数据接收，并通过回调进行处理
     */
    override fun response(data: String) {
        try {
            val jsonElement = JsonParser.parseString(data)
            val echo = jsonElement.asJsonObject["echo"]?.asString ?: error("错误的返回格式：NOT FOUND ECHO VALUE")
            // 查找回调
            callbacks[echo]?.let {
                if (it.cancel) {
                    callbacks.remove(echo)
                }else {
                    callbacks.remove(echo)
                    it.callback(jsonElement)
                }
            } ?: logger.warn("接收到事件[$echo]，但未找到对应处理器：$data")

        }catch (e: Exception) {
            logger.error("接口数据解析失败", e)
        }
    }

    override fun execute(request: OneBotRequestData) {
        apiWebSocket.sendMessage(request.websocket())
    }
}