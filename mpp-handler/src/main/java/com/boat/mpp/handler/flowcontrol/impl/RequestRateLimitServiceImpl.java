package com.boat.mpp.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.enums.RateLimitStrategy;
import com.boat.mpp.handler.flowcontrol.FlowControlParam;
import com.boat.mpp.handler.flowcontrol.FlowControlService;
import com.boat.mpp.handler.flowcontrol.annotations.LocalRateLimit;

/**
 *
 * @author boat
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitServiceImpl implements FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(1);
    }
}
