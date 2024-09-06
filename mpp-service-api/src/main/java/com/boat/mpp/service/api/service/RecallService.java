package com.boat.mpp.service.api.service;

import com.boat.mpp.service.api.domain.SendRequest;
import com.boat.mpp.service.api.domain.SendResponse;

/**
 * 撤回接口
 *
 * @author boat
 */
public interface RecallService {

    /**
     * 根据模板id撤回消息
     * @param request 参数
     * @return SendResponse
     */
    SendResponse recall(SendRequest request);
}
