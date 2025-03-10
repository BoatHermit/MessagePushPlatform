package com.boat.mpp.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.deduplication.DeduplicationRuleService;
import com.boat.mpp.handler.discard.DiscardMessageService;
import com.boat.mpp.handler.handler.HandlerHolder;
import com.boat.mpp.handler.shield.ShieldService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task 执行器
 * 0.丢弃消息
 * 2.屏蔽消息
 * 2.通用去重功能
 * 3.发送消息
 *
 * @author boat
 */
@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {

    private TaskInfo taskInfo;
    private DeduplicationRuleService deduplicationRuleService;
    private DiscardMessageService discardMessageService;
    private HandlerHolder handlerHolder;
    @Autowired
    private ShieldService shieldService;

    @Autowired
    public Task(DeduplicationRuleService deduplicationRuleService, DiscardMessageService discardMessageService
            , HandlerHolder handlerHolder) {
        this.deduplicationRuleService = deduplicationRuleService;
        this.discardMessageService = discardMessageService;
        this.handlerHolder = handlerHolder;
    }

    @Override
    public void run() {

        log.info("task:" + Thread.currentThread().getName());

        // 0. 丢弃消息
        if (discardMessageService.isDiscard(taskInfo)) {
           return;
        }

        // 1. 屏蔽消息
        shieldService.shield(taskInfo);

        // 2.平台通用去重
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            deduplicationRuleService.duplication(taskInfo);
        }

        // 3. 真正发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
        }

    }
}
