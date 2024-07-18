package org.sereinfish.catcat.framework.onebot.v11.connect.api

import com.google.gson.JsonElement
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.message.Message
import org.catcat.sereinfish.qqbot.universal.abstraction.layer.utils.UniversalId
import org.sereinfish.catcat.framework.onebot.v11.connect.api.entity.mate.*

/**
 * 各种接口实现的接口
 */
interface OneBotApi {
    /**
     * 发送私聊消息
     */
    suspend fun sendPrivateMsg(userId: UniversalId, message: Message, autoEscape: Boolean = true): Result<MessageReceiptMateData>

    /**
     * 发送群消息
     */
    suspend fun sendGroupMsg(group: UniversalId, message: Message, autoEscape: Boolean = true): Result<MessageReceiptMateData>

    /**
     * 发送消息
     */
    suspend fun sendMsg(
        messageType: String,
        userId: UniversalId?,
        group: UniversalId? = null,
        message: Message,
        autoEscape: Boolean = true
    ): Result<MessageReceiptMateData>

    /**
     * 撤回消息
     */
    suspend fun deleteMsg(messageId: UniversalId): Boolean

    /**
     * 获取消息
     */
    suspend fun getMsg(messageId: UniversalId): Result<GetMessageMateData>

    /**
     * 获取合并转发消息
     */
    suspend fun getForwardMsg(id: String): Result<ForwardMsgInfoMateData>

    /**
     * 发送好友赞
     */
    suspend fun sendLike(userId: UniversalId, times: Int): Boolean

    /**
     * 群组踢人
     */
    suspend fun groupKick(group: UniversalId, userId: UniversalId, rejectAddRequest: Boolean): Boolean

    /**
     * 群组禁言
     */
    suspend fun groupBan(group: UniversalId, userId: UniversalId, duration: Long): Boolean

    /**
     * 全体禁言
     */
    suspend fun groupWholeBan(group: UniversalId, enable: Boolean): Boolean

    /**
     * 群组设置管理员
     */
    suspend fun setGroupAdmin(group: UniversalId, userId: UniversalId, enable: Boolean): Boolean

    /**
     * 设置群组匿名
     */
    suspend fun setGroupAnonymous(group: UniversalId, enable: Boolean): Boolean

    /**
     * 设置群名
     */
    suspend fun setGroupName(group: UniversalId, name: String): Boolean

    /**
     * 退出群组
     */
    suspend fun groupLeave(group: UniversalId, isDismiss: Boolean): Boolean

    /**
     * 设置群组专属头衔
     */
    suspend fun setGroupSpecialTitle(group: UniversalId, userId: UniversalId, specialTitle: String, duration: Long): Boolean

    /**
     * 处理加好友请求
     */
    suspend fun handleFriendAddRequest(flag: String, approve: Boolean, remark: String): Boolean

    /**
     * 处理加群请求或邀请
     */
    suspend fun handleGroupAddRequest(flag: String, type: String, approve: Boolean, reason: String): Boolean

    /**
     * 群组匿名禁言
     */
    suspend fun groupAnonymousBan(group: UniversalId, anonymousFlag: String, duration: Long): Boolean

    /**
     * 获取登录号信息
     */
    suspend fun getLoginInfo(): Result<LoginBotInfoMateData>

    /**
     * 获取陌生人信息
     */
    suspend fun getStrangerInfo(userId: UniversalId, noCache: Boolean = false): Result<UserInfoMateData>

    /**
     * 获取好友列表
     */
    suspend fun getFriendList(): Result<FriendListMateData>

    /**
     * 获取群信息
     */
    suspend fun getGroupInfo(group: UniversalId, noCache: Boolean = false): Result<GroupInfoMateData>

    /**
     * 获取群
     */
    suspend fun getGroupList(): Result<GroupListInfoMateData>

    /**
     * 获取群成员信息
     */
    suspend fun getGroupMemberInfo(group: UniversalId, userId: UniversalId, noCache: Boolean = false): Result<GroupMemberInfoMateData>

    /**
     * 获取群成员列表
     */
    suspend fun getGroupMemberList(group: UniversalId): Result<GroupMemberListInfoMateData>

    /**
     * 获取群荣誉信息
     */
    suspend fun getGroupHonorInfo(group: UniversalId, type: String): JsonElement

    /**
     * 获取Cookies
     */
    suspend fun getCookies(domain: String): JsonElement

    /**
     * 获取CsrfToken
     */
    suspend fun getCsrfToken(): JsonElement

    /**
     * 获取语音
     */
    suspend fun getRecord(file: String, outFormat: String): JsonElement

    /**
     * 获取图片
     */
    suspend fun getImage(file: String): JsonElement

    /**
     * 检查是否可以发送图片
     */
    suspend fun canSendImage(): Boolean

    /**
     * 检查是否可以发送语音
     */
    suspend fun canSendRecord(): Boolean

    /**
     * 获取运行状态
     */
    suspend fun status(): JsonElement

    /**
     * 获取版本信息
     */
    suspend fun versionInfo(): JsonElement

    /**
     * 重启onebot实现
     */
    suspend fun reStart(delay: Long): Boolean

    /**
     * 清理缓存
     */
    suspend fun cleanCache(): Boolean
}