package com.boat.mpp.handler.deduplication.build.impl;

import com.alibaba.fastjson.JSONObject;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.deduplication.DeduplicationHolder;
import com.boat.mpp.handler.deduplication.DeduplicationParam;
import com.boat.mpp.handler.deduplication.build.DeduplicationBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Objects;

public abstract class AbstractDeduplicationBuilder implements DeduplicationBuilder {
    protected Integer deduplicationType;

    private final DeduplicationHolder deduplicationHolder;

    @Autowired
    AbstractDeduplicationBuilder(DeduplicationHolder deduplicationHolder) {
        this.deduplicationHolder = deduplicationHolder;
    }

    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig, TaskInfo taskInfo) {
        JSONObject object = JSONObject.parseObject(duplicationConfig);
        if (Objects.isNull(object)) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(
                object.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }
}
