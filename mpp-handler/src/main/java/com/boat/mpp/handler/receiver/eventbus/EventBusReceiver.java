package com.boat.mpp.handler.receiver.eventbus;

import com.boat.mpp.handler.service.ConsumeService;
import com.google.common.eventbus.Subscribe;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.constans.MessageQueuePipeline;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.support.mq.eventbus.EventBusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author boat
 */
@Component
@ConditionalOnProperty(name = "mpp.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
@Slf4j
public class EventBusReceiver implements EventBusListener {

    @Autowired
    private ConsumeService consumeService;

    @Override
    @Subscribe
    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);
    }

    @Override
    @Subscribe
    public void recall(MessageTemplate messageTemplate){

    }
}
