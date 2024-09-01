package com.boat.mpp.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.boat.mpp.common.enums.RespStatusEnum;
import com.boat.mpp.common.vo.BasicResultVO;
import com.boat.mpp.service.api.domain.MessageParam;
import com.boat.mpp.service.api.impl.domain.SendTaskModel;
import com.boat.mpp.support.pipeline.BusinessProcess;
import com.boat.mpp.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前置参数校验
 *
 * @author boat
 */
@Slf4j
@Service
public class PreParamCheckAction implements BusinessProcess<SendTaskModel> {

    /**
     * 最大的人数
     */
    private static final Integer BATCH_RECEIVER_SIZE = 100;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        // 1.没有传入 消息模板Id 或者 messageParam
        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 2.过滤 receiver=null 的messageParam
        List<MessageParam> resultMessageParamList = messageParamList.stream()
                .filter(messageParam -> !StrUtil.isBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(resultMessageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }
        sendTaskModel.setMessageParamList(resultMessageParamList);

        // 3.过滤receiver大于100的请求
        if (messageParamList.stream().anyMatch(messageParam -> messageParam.getReceiver()
                .split(StrUtil.COMMA).length > BATCH_RECEIVER_SIZE)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TOO_MANY_RECEIVER));
            return;
        }

    }
}
