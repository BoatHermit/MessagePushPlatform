package com.boat.mpp.handler.deduplication.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.deduplication.DeduplicationHolder;
import com.boat.mpp.handler.deduplication.DeduplicationParam;
import com.boat.mpp.handler.deduplication.limit.LimitService;
import com.boat.mpp.handler.deduplication.service.DeduplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 去重服务
 * @author boat
 */
public abstract class AbstractDeduplicationService implements DeduplicationService {

    protected Integer deduplicationType;

    protected LimitService limitService;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }


    @Override
    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();

        Set<String> filterReceiver = limitService.limitFilter(this, taskInfo, param);

        // 剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
        }
    }


    /**
     * 构建去重的Key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);


}
