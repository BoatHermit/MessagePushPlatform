package com.boat.mpp.handler.deduplication.build.impl;

import cn.hutool.core.date.DateUtil;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.enums.AnchorState;
import com.boat.mpp.common.enums.DeduplicationType;
import com.boat.mpp.handler.deduplication.DeduplicationHolder;
import com.boat.mpp.handler.deduplication.DeduplicationParam;
import com.boat.mpp.handler.deduplication.build.DeduplicationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder {

    @Autowired
    public FrequencyDeduplicationBuilder(DeduplicationHolder deduplicationHolder) {
        super(deduplicationHolder);
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
