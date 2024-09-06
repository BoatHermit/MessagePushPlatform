package com.boat.mpp.handler.deduplication.build.impl;

import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.enums.AnchorState;
import com.boat.mpp.common.enums.DeduplicationType;
import com.boat.mpp.handler.deduplication.DeduplicationHolder;
import com.boat.mpp.handler.deduplication.DeduplicationParam;
import com.boat.mpp.handler.deduplication.build.DeduplicationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder  {

    @Autowired
    public ContentDeduplicationBuilder(DeduplicationHolder deduplicationHolder) {
        super(deduplicationHolder);
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
