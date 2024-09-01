package com.boat.mpp.service.api.service;

import com.boat.mpp.service.api.domain.BatchSendRequest;
import com.boat.mpp.service.api.domain.SendRequest;
import com.boat.mpp.service.api.domain.SendResponse;

/**
 * 发送接口
 *
 * @author boat
 */
public interface SendService {

    /**
     * 单发发送接口
     * @param request SendRequest
     * @return SendResponse
     */
    SendResponse send(SendRequest request);

    /**
     * 群发发送接口
     * @param request SendRequest
     * @return SendResponse
     */
    SendResponse batchSend(BatchSendRequest request);
}
