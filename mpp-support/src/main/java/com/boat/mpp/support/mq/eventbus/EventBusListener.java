package com.boat.mpp.support.mq.eventbus;


import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.domain.MessageTemplate;

import java.util.List;

/**
 * 监听器
 *
 * @author boat
 */
public interface EventBusListener {


    /**
     * 消费消息
     * @param lists
     */
    void consume(List<TaskInfo> lists);

    /**
     * 撤回消息
     * @param messageTemplate
     */
    void recall(MessageTemplate messageTemplate);
}
