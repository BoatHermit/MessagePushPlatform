package com.boat.mpp.cron.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.config.SupportThreadPoolConfig;
import com.boat.mpp.support.utils.RedisUtils;
import com.google.common.base.Throwables;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 夜间屏蔽的延迟处理类
 * <p>
 * example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送
 *
 * @author boat
 */
@Service
@Slf4j
public class NightShieldLazyPendingHandler {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${mpp.business.topic.name}")
    private String topicName;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 处理 夜间屏蔽(次日早上9点发送的任务)
     */
    @XxlJob("nightShieldLazyJob")
    public void execute() {
        log.info("NightShieldLazyPendingHandler#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (redisUtils.lLen(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY) > 0) {
                String taskInfo = redisUtils.lPop(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY);
                if (StrUtil.isNotBlank(taskInfo)) {
                    try {
                        kafkaTemplate.send(topicName, JSON.toJSONString(
                                Arrays.asList(JSON.parseObject(taskInfo, TaskInfo.class))
                                , SerializerFeature.WriteClassName));
                    } catch (Exception e) {
                        log.error("nightShieldLazyJob send kafka fail! e:{},params:{}"
                                , Throwables.getStackTraceAsString(e), taskInfo);
                    }
                }
            }
        });
    }
}
