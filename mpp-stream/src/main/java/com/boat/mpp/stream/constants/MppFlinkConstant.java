package com.boat.mpp.stream.constants;

/**
 * Flink常量信息
 *
 * @author boat
 */
public class MppFlinkConstant {

    /**
     * Kafka 配置信息
     * TODO 使用前配置kafka broker ip:port
     * (真实网络ip,这里不能用配置的hosts，看语雀文档得到真实ip)
     * （如果想要自己监听到所有的消息，改掉groupId）
     */
    public static final String GROUP_ID = "mppLogGroup";
    public static final String TOPIC_NAME = "mppTraceLog";
    public static final String BROKER = "mpp-kafka:9092";

    /**
     * redis 配置
     * TODO 使用前配置redis ip:port
     * (真实网络ip,这里不能用配置的hosts，看语雀文档得到真实ip)
     */
    public static final String REDIS_IP = "mpp-redis";
    public static final String REDIS_PORT = "6379";
    public static final String REDIS_PASSWORD = "mpp";


    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "mpp_kafka_source";
    public static final String FUNCTION_NAME = "mpp_transfer";
    public static final String SINK_NAME = "mpp_sink";
    public static final String JOB_NAME = "mppBootStrap";
}
