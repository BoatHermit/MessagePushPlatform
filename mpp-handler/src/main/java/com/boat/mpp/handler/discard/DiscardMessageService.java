package com.boat.mpp.handler.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boat.mpp.common.constant.CommonConstant;
import com.boat.mpp.common.domain.AnchorInfo;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.enums.AnchorState;
import com.boat.mpp.support.service.ConfigService;
import com.boat.mpp.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 丢弃模板消息
 *
 * @author 3y.
 */
@Service
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discardMsgIds";
    private final ConfigService config;
    private final LogUtils logUtils;

    @Autowired
    public DiscardMessageService(ConfigService config, LogUtils logUtils) {
        this.config = config;
        this.logUtils = logUtils;
    }


    /**
     * 丢弃消息，配置在apollo
     *
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        // 配置示例:	["1","2"]
        JSONArray array = JSON.parseArray(
                config.getProperty(DISCARD_MESSAGE_KEY, CommonConstant.EMPTY_VALUE_JSON_ARRAY));

        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            logUtils.print(AnchorInfo.builder().state(AnchorState.DISCARD.getCode())
                    .businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            return true;
        }
        return false;
    }
}