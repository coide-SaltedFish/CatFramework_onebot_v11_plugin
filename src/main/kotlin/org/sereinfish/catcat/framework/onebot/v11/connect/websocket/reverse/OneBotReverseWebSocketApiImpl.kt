package org.sereinfish.catcat.framework.onebot.v11.connect.websocket.reverse

import com.google.gson.JsonElement
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.cat.frame.event.EventManager
import org.sereinfish.cat.frame.utils.logger
import org.sereinfish.cat.frame.utils.toJson
import org.sereinfish.catcat.framework.onebot.v11.connect.api.OneBotApi
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate.*
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.parser.ApiResponseParser
import org.sereinfish.catcat.framework.onebot.v11.connect.request.OneBotRequestData
import org.sereinfish.catcat.framework.onebot.v11.events.message.send.OneBotPrivateSendingEvent
import org.sereinfish.catcat.framework.onebot.v11.utils.*
import kotlin.coroutines.resume

internal class OneBotReverseWebSocketApiImpl: OneBotApi {
    private val logger = logger()

    private val _webSocket: OneBotReverseWebSocket get() = webSocket ?: error("尚未配置 ReverseWebSocket : NULL")
    var webSocket: OneBotReverseWebSocket? = null

    override suspend fun sendPrivateMsg(
        userId: UniversalId,
        message: Message,
        autoEscape: Boolean
    ): Result<MessageReceiptMateData> {
        return sendData(MessageReceiptMateData, "send_private_msg", 120 * 1000) {
            "user_id" to userId.id
            "message" to message.encode()
            "auto_escape" to autoEscape
        }
    }

    override suspend fun sendGroupMsg(
        group: UniversalId,
        message: Message,
        autoEscape: Boolean
    ): Result<MessageReceiptMateData> {
        return sendData(MessageReceiptMateData, "send_group_msg", 120 * 1000) {
            "group_id" to group.id
            "message" to message.encode()
            "auto_escape" to autoEscape
        }
    }

    override suspend fun sendMsg(
        messageType: String,
        userId: UniversalId?,
        group: UniversalId?,
        message: Message,
        autoEscape: Boolean
    ): Result<MessageReceiptMateData> {
        return sendData(MessageReceiptMateData, "send_msg", 120 * 1000) {
            "message_type" to messageType
            userId?.let {
                "user_id" to it.id
            }
            group?.let {
                "group_id" to it.id
            }
            "message" to message.encode()
            "auto_escape" to autoEscape
        }
    }

    override suspend fun deleteMsg(messageId: UniversalId): Boolean {
        return sendData(EmptyData, "delete_msg"){
            "message_id" to messageId.id
        }.getOrThrow().isSuccess()
    }

    override suspend fun getMsg(messageId: UniversalId): Result<GetMessageMateData> {
        return sendData(GetMessageMateData, "get_msg") {
            "message_id" to messageId.id
        }
    }

    /**
     * 获取合并转发消息内容
     */
    override suspend fun getForwardMsg(id: String): Result<ForwardMsgInfoMateData> {
        return sendData(ForwardMsgInfoMateData, "get_forward_msg") {
            "id" to id
        }
    }

    override suspend fun sendLike(userId: UniversalId, times: Int): Boolean {
        return sendData(EmptyData, "send_like"){
            "user_id" to userId.id
            "times" to times
        }.getOrThrow().isSuccess()
    }

    override suspend fun groupKick(group: UniversalId, userId: UniversalId, rejectAddRequest: Boolean): Boolean {
        return sendData(EmptyData, "set_group_kick"){
            "group_id" to group.id
            "user_id" to userId.id
            "reject_add_request" to rejectAddRequest
        }.getOrThrow().isSuccess()
    }

    override suspend fun groupBan(group: UniversalId, userId: UniversalId, duration: Long): Boolean {
        return sendData(EmptyData, "set_group_ban"){
            "group_id" to group.id
            "user_id" to userId.id
            "duration" to duration
        }.getOrThrow().isSuccess()
    }

    override suspend fun groupWholeBan(group: UniversalId, enable: Boolean): Boolean {
        return sendData(EmptyData, "set_group_whole_ban"){
            "group_id" to group.id
            "enable" to enable
        }.getOrThrow().isSuccess()
    }

    override suspend fun setGroupAdmin(group: UniversalId, userId: UniversalId, enable: Boolean): Boolean {
        return sendData(EmptyData, "set_group_admin"){
            "group_id" to group.id
            "user_id" to userId.id
            "enable" to enable
        }.getOrThrow().isSuccess()
    }

    override suspend fun setGroupAnonymous(group: UniversalId, enable: Boolean): Boolean {
        return sendData(EmptyData, "set_group_anonymous"){
            "group_id" to group.id
            "enable" to enable
        }.getOrThrow().isSuccess()
    }

    override suspend fun setGroupName(group: UniversalId, name: String): Boolean {
        return sendData(EmptyData, "set_group_name"){
            "group_id" to group.id
            "name" to name
        }.getOrThrow().isSuccess()
    }

    override suspend fun groupLeave(group: UniversalId, isDismiss: Boolean): Boolean {
        return sendData(EmptyData, "set_group_leave"){
            "group_id" to group.id
            "is_dismiss" to isDismiss
        }.getOrThrow().isSuccess()
    }

    override suspend fun setGroupSpecialTitle(
        group: UniversalId,
        userId: UniversalId,
        specialTitle: String,
        duration: Long
    ): Boolean {
        return sendData(EmptyData, "set_group_special_title"){
            "group_id" to group.id
            "user_id" to userId.id
            "special_title" to specialTitle
            "duration" to duration
        }.getOrThrow().isSuccess()
    }

    override suspend fun handleFriendAddRequest(flag: String, approve: Boolean, remark: String): Boolean {
        return sendData(EmptyData, "set_friend_add_request"){
            "flag" to flag
            "approve" to approve
            "remark" to remark
        }.getOrThrow().isSuccess()
    }

    override suspend fun handleGroupAddRequest(flag: String, type: String, approve: Boolean, reason: String): Boolean {
        return sendData(EmptyData, "set_group_add_request"){
            "flag" to flag
            "type" to type
            "approve" to approve
            "reason" to reason
        }.getOrThrow().isSuccess()
    }

    override suspend fun groupAnonymousBan(group: UniversalId, anonymousFlag: String, duration: Long): Boolean {
        return sendData(EmptyData, "set_group_anonymous_ban"){
            "group_id" to group.id
            "anonymous_flag" to anonymousFlag
            "duration" to duration
        }.getOrThrow().isSuccess()
    }

    override suspend fun getLoginInfo(): Result<LoginBotInfoMateData> {
        return sendData(LoginBotInfoMateData, "get_login_info")
    }

    override suspend fun getStrangerInfo(userId: UniversalId, noCache: Boolean): Result<UserInfoMateData> {
        return sendData(UserInfoMateData, "get_stranger_info") {
            "user_id" to userId.id
            "no_cache" to noCache
        }
    }

    override suspend fun getFriendList(): Result<FriendListMateData> {
        return sendData(FriendListMateData, "get_friend_list", 60 * 1000)
    }

    override suspend fun getGroupInfo(group: UniversalId, noCache: Boolean): Result<GroupInfoMateData> {
        return sendData(GroupInfoMateData, "get_group_info"){
            "group_id" to group.id
            "no_cache" to noCache
        }
    }

    override suspend fun getGroupList(): Result<GroupListInfoMateData> {
        return sendData(GroupListInfoMateData, "get_group_list", 60 * 1000)
    }

    override suspend fun getGroupMemberInfo(
        group: UniversalId,
        userId: UniversalId,
        noCache: Boolean
    ): Result<GroupMemberInfoMateData> {
        return sendData(GroupMemberInfoMateData, "get_group_member_info"){
            "group_id" to group.id
            "user_id" to userId.id
            "no_cache" to noCache
        }
    }

    override suspend fun getGroupMemberList(group: UniversalId): Result<GroupMemberListInfoMateData> {
        return sendData(GroupMemberListInfoMateData, "get_group_member_list", 60 * 1000){
            "group_id" to group.id
        }
    }

    override suspend fun getGroupHonorInfo(group: UniversalId, type: String): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun getCookies(domain: String): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun getCsrfToken(): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun getRecord(file: String, outFormat: String): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun getImage(file: String): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun canSendImage(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun canSendRecord(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun status(): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun versionInfo(): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun reStart(delay: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun cleanCache(): Boolean {
        TODO("Not yet implemented")
    }

    private suspend inline fun <T> sendData(
        parser: ApiResponseParser<T>,
        action: String,
        outTime: Long = 15000,
        noinline params: MapBuilder<String, Any>.() -> Unit = {},
    ): Result<T> = try {
        withTimeout(outTime) {
            suspendCancellableCoroutine { continuation ->
                val data = OneBotRequestData(action, buildCatMap(params))
                // 构建回调
                val callback = object : ReverseWebSocketCallback {
                    override val uuid: String = data.echo
                    override var cancel: Boolean = false

                    override fun callback(data: JsonElement) {
                        // 收到数据
                        try {
                            if (data.asJsonObject["retcode"].asInt != 0) {
                                logger.debug(data.toJson())
                                logger.error("接口返回异常：{action:$action,params:${buildCatMap(params).toJson()}}")
                                error(
                                    "接口请求失败，结果返回[${
                                        data.asJsonObjectOrNUll?.get("status")?.asStringOrNull
                                    }(${
                                        data.asJsonObjectOrNUll?.get("retcode")?.asIntOrNull
                                    })]: ${
                                        data.asJsonObjectOrNUll?.get("message")?.asStringOrNull
                                    }"
                                )
                            }

                            val result = parser.parser(data)
                            continuation.resume(Result.success(result))
                        } catch (e: Exception) {
                            continuation.resume(Result.failure(e))
                        }
                    }
                }
                _webSocket.addCallback(callback)
                // 发送执行数据
                _webSocket.execute(data)

                continuation.invokeOnCancellation {
                    callback.cancel = true
                }
            }
        }
    }catch (e: TimeoutCancellationException) {
        throw RuntimeException("{action:$action,params:${buildCatMap(params).toJson()}}", e)
    }
}