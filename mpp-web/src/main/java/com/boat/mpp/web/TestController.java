package com.boat.mpp.web;

import com.alibaba.fastjson.JSON;
import com.boat.mpp.service.api.domain.MessageParam;
import com.boat.mpp.service.api.domain.SendRequest;
import com.boat.mpp.service.api.domain.SendResponse;
import com.boat.mpp.service.api.enums.BusinessCode;
import com.boat.mpp.service.api.service.SendService;
import com.boat.mpp.support.dao.MessageTemplateDao;
import com.boat.mpp.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Boat
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private SendService sendService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/test")
    private String test() {
        System.out.println("sout: Hello World");
        log.info("log: Hello World");
        return "Hello!";
    }
    @RequestMapping("/database")
    private String testDataBase() {
        List<MessageTemplate> list = messageTemplateDao
                .findAllByIsDeletedEquals(0, PageRequest.of(0, 10));
        return JSON.toJSONString(list);
    }
    @RequestMapping("/redis")
    private String testRedis() {
        stringRedisTemplate.opsForValue().set("boat", "mpp");
        return stringRedisTemplate.opsForValue().get("boat");
    }

    @RequestMapping("/send")
    private String testSend() {
        SendRequest sendRequest = SendRequest.builder()
                .code(BusinessCode.COMMON_SEND.getCode())
                .messageTemplateId(2L)
                .messageParam(MessageParam.builder().receiver("yinzihang02@163.com").build()).build();

        SendResponse response = sendService.send(sendRequest);
        return JSON.toJSONString(response);

    }
}
