package com.boat.mpp.handler.deduplication;

import com.boat.mpp.common.constant.CommonConstant;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.enums.DeduplicationType;
import com.boat.mpp.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 去重服务
 *
 * @author boat
 */
@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplicationRule";
    private final DeduplicationHolder deduplicationHolder;
    private final ConfigService config;

    @Autowired
    DeduplicationRuleService(DeduplicationHolder deduplicationHolder, ConfigService config) {
        this.deduplicationHolder = deduplicationHolder;
        this.config = config;
    }


    public void duplication(TaskInfo taskInfo) {
        // 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, CommonConstant.EMPTY_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder
                    .selectBuilder(deduplicationType).build(deduplicationConfig, taskInfo);
            if (Objects.nonNull(deduplicationParam)) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }
}
