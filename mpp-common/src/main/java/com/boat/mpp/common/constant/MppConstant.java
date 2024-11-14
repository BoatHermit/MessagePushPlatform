package com.boat.mpp.common.constant;


/**
 * 基础的常量信息
 *
 * @author boat
 */
public class MppConstant {

    /**
     * businessId默认的长度
     * 生成的逻辑：com.boat.mpp.support.utils.TaskInfoUtils#generateBusinessId(java.lang.Long, java.lang.Integer)
     */
    public final static Integer BUSINESS_ID_LENGTH = 16;

    /**
     * 接口限制 最多的人数
     */
    public static final Integer BATCH_RECEIVER_SIZE = 100;

    /**
     * 消息发送给全部人的标识
     * (企业微信 应用消息)
     * (钉钉自定义机器人)
     * (钉钉工作消息)
     */
    public static final String SEND_ALL = "@all";


    /**
     * 默认的常量，如果新建模板/账号时，没传入则用该常量
     */
    public static final String DEFAULT_CREATOR = "Boat";
    public static final String DEFAULT_UPDATOR = "Boat";
    public static final String DEFAULT_TEAM = "木舟居士公众号";
    public static final String DEFAULT_AUDITOR = "Boat";

    /**
     * 链路追踪缓存的key标识
     */
    public static final String CACHE_KEY_PREFIX = "Boat";
    public static final String MESSAGE_ID = "MessageId";
}
