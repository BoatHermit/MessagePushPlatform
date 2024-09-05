package com.boat.mpp.handler.handler;

import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.support.domain.MessageTemplate;

public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param messageTemplate
     * @return
     */
    void recall(MessageTemplate messageTemplate);

}