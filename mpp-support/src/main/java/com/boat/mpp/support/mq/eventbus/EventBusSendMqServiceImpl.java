package com.boat.mpp.support.mq.eventbus;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.constans.MessageQueuePipeline;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;


/**
 * EventBus 发送实现类
 *
 * @author boat
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "mpp.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusSendMqServiceImpl implements SendMqService {
    private EventBus eventBus = new EventBus();

    @Autowired
    private EventBusListener eventBusListener;
    @Value("${mpp.business.topic.name}")
    private String sendTopic;
    @Value("${mpp.business.recall.topic.name}")
    private String recallTopic;
    /**
     * 单机 队列默认不支持 tagId过滤（单机无必要）
     * @param topic
     * @param jsonValue
     * @param tagId
     */
    @Override
    public void send(String topic, String jsonValue, String tagId) {
        eventBus.register(eventBusListener);
        if (topic.equals(sendTopic)) {
            eventBus.post(JSON.parseArray(jsonValue, TaskInfo.class));
        } else if (topic.equals(recallTopic)) {
            eventBus.post(JSON.parseObject(jsonValue, MessageTemplate.class));
        }
    }
    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
