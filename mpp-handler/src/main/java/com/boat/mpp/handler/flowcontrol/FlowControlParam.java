package com.boat.mpp.handler.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import com.boat.mpp.handler.enums.RateLimitStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流量控制所需要的参数
 * <p>
 * @author boat
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {

    /**
     * 限流器
     * 子类初始化的时候指定
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器初始限流大小
     * 子类初始化的时候指定
     */
    protected Double rateInitValue;

    /**
     * 限流的策略
     * 子类初始化的时候指定
     */
    protected RateLimitStrategy rateLimitStrategy;
}
