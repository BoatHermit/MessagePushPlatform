package com.boat.mpp.handler.shield;

import com.boat.mpp.common.domain.TaskInfo;

/**
 * 屏蔽服务
 *
 * @author boat
 */
public interface ShieldService {


    /**
     * 屏蔽消息
     *
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}
