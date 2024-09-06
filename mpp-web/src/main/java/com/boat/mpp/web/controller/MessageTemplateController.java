package com.boat.mpp.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.boat.mpp.common.enums.RespStatusEnum;
import com.boat.mpp.service.api.domain.MessageParam;
import com.boat.mpp.service.api.domain.SendRequest;
import com.boat.mpp.service.api.domain.SendResponse;
import com.boat.mpp.service.api.enums.BusinessCode;
import com.boat.mpp.service.api.service.SendService;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.web.exception.CommonException;
import com.boat.mpp.web.service.MessageTemplateService;
import com.boat.mpp.web.utils.Convert4Amis;
import com.boat.mpp.web.vo.MessageTemplateParam;
import com.boat.mpp.web.vo.MessageTemplateVo;
import com.boat.mpp.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 消息模板管理Controller
 */
@Api(tags = "消息模板管理")
@Slf4j
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

//    @Autowired
//    private RecallService recallService;

//    @Autowired
//    private LoginUtils loginUtils;

    @Value("${mpp.business.upload.crowd.path}")
    private String dataPath;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    public MessageTemplate saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
//        if (loginUtils.needLogin() && StrUtil.isBlank(messageTemplate.getCreator())) {
//            throw new CommonException(RespStatusEnum.NO_LOGIN.getCode(), RespStatusEnum.NO_LOGIN.getMsg());
//        }
        return messageTemplateService.saveOrUpdate(messageTemplate);
    }

    /**
     * 列表数据
     */
    @GetMapping("/list")
    public MessageTemplateVo queryList(@Validated MessageTemplateParam messageTemplateParam) {
//        if (loginUtils.needLogin() && StrUtil.isBlank(messageTemplateParam.getCreator())) {
//            throw new CommonException(RespStatusEnum.NO_LOGIN.getCode(), RespStatusEnum.NO_LOGIN.getMsg());
//        }
        Page<MessageTemplate> messageTemplates = messageTemplateService.queryList(messageTemplateParam);
        List<Map<String, Object>> result = Convert4Amis.flatListMap(messageTemplates.toList());
        return MessageTemplateVo.builder().count(messageTemplates.getTotalElements()).rows(result).build();
    }

    /**
     * 根据Id查找
     */
    @GetMapping("query/{id}")
    public Map<String, Object> queryById(@PathVariable("id") Long id) {
        return Convert4Amis.flatSingleMap(messageTemplateService.queryById(id));
    }

    /**
     * 根据Id复制
     */
    @PostMapping("copy/{id}")
    public void copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
    }


    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            messageTemplateService.deleteByIds(idList);
        }
    }


    /**
     * 测试发送接口
     */
    @PostMapping("test")
    public SendResponse test(@RequestBody MessageTemplateParam messageTemplateParam) {

        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();
        SendResponse response = sendService.send(sendRequest);
        if (!Objects.equals(response.getCode(), RespStatusEnum.SUCCESS.getCode())) {
            throw new CommonException(response.getMsg());
        }
        return response;
    }

    /**
     * 获取需要测试的模板占位符，透出给Amis
     */
    @PostMapping("test/content")
    public CommonAmisVo test(Long id) {
        MessageTemplate messageTemplate = messageTemplateService.queryById(id);
        return Convert4Amis.getTestContent(messageTemplate.getMsgContent());
    }





}

