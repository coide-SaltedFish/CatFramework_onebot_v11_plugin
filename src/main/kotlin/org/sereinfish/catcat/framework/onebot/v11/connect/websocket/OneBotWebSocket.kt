package org.sereinfish.catcat.framework.onebot.v11.connect.websocket

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.ByteString
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.nonNull
import java.nio.charset.Charset

class OneBotWebSocket(
    private val client: OkHttpClient,
    private val url: String,
    private val onMessage: (String) -> Unit,
) {
    private val logger = logger()
    private var signal: CompletableDeferred<Result<Unit>>? = null

    val webSocket: WebSocket get() = mWebSocket ?: error("WebSocket 尚未连接")
    private var mWebSocket: WebSocket? = null
    private var isConntect = false

    private var reTryConnectCount = 0
    private val reTryConnectDelays = arrayOf(0L, 5, 10, 15, 20, 40, 60)

    fun connect(): Boolean {
        if (isConnect()){
            logger.warn("WebSocket 已连接，此次连接取消")
            return false
        }

        signal = CompletableDeferred()
        client.newWebSocket(
            Request.Builder()
                .url(url)
                .build(),
            createListener()
        )
        // 等待连接返回
        runBlocking {
            signal?.await()?.getOrThrow()
        }

        return true
    }

    fun isConnect(): Boolean {
        return mWebSocket.nonNull() && isConntect
    }

    /**
     * 重连
     */
    private fun reConnect() {
        if (reTryConnectCount < reTryConnectDelays.size) {
            logger.info("尝试重连 ${reTryConnectCount + 1}/${reTryConnectDelays.size}")
            Thread.sleep(reTryConnectDelays[reTryConnectCount++] * 1000)
            connect()
        }else {
            logger.error("已达到最大重试次数，连接已断开 $reTryConnectDelays/${reTryConnectDelays.size}")
        }
    }

    fun sendMessage(text: String): Boolean {
        if (isConnect().not()) return false
        return webSocket.send(text)
    }
    fun sendMessage(data: ByteString): Boolean {
        if (isConnect().not()) return false
        return webSocket.send(data)
    }

    fun close() {
        if (isConnect()) {
            webSocket.cancel()
            webSocket.close(1001, "客户端主动关闭连接")
            mWebSocket = null
        }
    }

    fun response(data: String) {
        onMessage(data)
    }

    private fun createListener() = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            logger.warn("WebSocket Closed[$code]: $reason")
            mWebSocket = null
            isConntect = false
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            logger.warn("WebSocket Closing[$code]: $reason")
            mWebSocket = null
            isConntect = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)

            logger.info("Websocket 连接失败：${response?.message}")
            logger.error("Websocket 连接异常", t)
            isConntect = false

            reConnect()
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            try {
                response(bytes.string(Charset.defaultCharset()))
            }catch (e: Exception) {
                logger.error("Websocket 已捕获消息处理异常", e)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            try {
                response(text)
            }catch (e: Exception) {
                logger.error("Websocket 已捕获消息处理异常", e)
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            logger.info("Open Websocket $response")
            mWebSocket = webSocket
            isConntect = response.code == 101
            if (isConntect.not()) {
                reConnect()
            }else {
                logger.info("Websocket connect success!")
                signal?.complete(Result.success(Unit))
            }
        }
    }
}