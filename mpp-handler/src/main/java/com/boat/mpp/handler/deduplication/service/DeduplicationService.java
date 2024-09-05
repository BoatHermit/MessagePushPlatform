package com.boat.mpp.handler.deduplication.service;

import com.boat.mpp.handler.deduplication.DeduplicationParam;

public interface DeduplicationService {

    /**
     * 去重
     */
    void deduplication(DeduplicationParam param);
}
