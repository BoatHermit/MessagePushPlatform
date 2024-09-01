package com.boat.mpp.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.boat.mpp.common.enums.RespStatusEnum;
import com.boat.mpp.common.vo.BasicResultVO;
import com.boat.mpp.service.api.enums.BusinessCode;
import com.boat.mpp.service.api.impl.domain.SendTaskModel;
import com.boat.mpp.support.mq.SendMqService;
import com.boat.mpp.support.pipeline.BusinessProcess;
import com.boat.mpp.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 将消息发送到MQ
 *
 * @author boat
 */
@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {


    @Autowired
    private SendMqService sendMqService;

    @Value("${mpp.business.topic.name}")
    private String sendMessageTopic;

    @Value("${mpp.business.recall.topic.name}")
    private String mppRecall;
    @Value("${mpp.business.tagId.value}")
    private String tagId;

    @Value("${mpp.mq.pipeline}")
    private String mqPipeline;


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        try {
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getTaskInfo()
                        , new SerializerFeature[]{SerializerFeature.WriteClassName});
                sendMqService.send(sendMessageTopic, message, tagId);
            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getMessageTemplate()
                        , new SerializerFeature[]{SerializerFeature.WriteClassName});
                sendMqService.send(mppRecall, message, tagId);
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
        }
    }

}
