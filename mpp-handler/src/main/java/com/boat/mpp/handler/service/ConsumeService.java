package com.boat.mpp.handler.service;


import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.domain.MessageTemplate;

import java.util.List;

/**
 * 消费消息服务
 *
 * @author boat
 */
public interface ConsumeService {

    /**
     * 从 MQ 拉到消息进行消费，发送消息
     *
     * @param taskInfoLists
     */
    void consume2Send(List<TaskInfo> taskInfoLists);


    /**
     * 从 MQ 拉到消息进行消费，撤回消息
     *
     * @param messageTemplate
     */
    void consume2recall(MessageTemplate messageTemplate);


}
