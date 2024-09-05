package com.boat.mpp.handler.deduplication.build;

import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.deduplication.DeduplicationParam;

public interface DeduplicationBuilder {

    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     *
     * @param deduplication
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
