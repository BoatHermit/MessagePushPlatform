package com.boat.mpp.web;

import com.alibaba.fastjson.JSON;
import com.boat.mpp.support.dao.MessageTemplateDao;
import com.boat.mpp.support.entity.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Boat
 */
@RestController
@Slf4j
public class TestController {

    private final MessageTemplateDao messageTemplateDao;

    @Autowired
    TestController(MessageTemplateDao messageTemplateDao) {
        this.messageTemplateDao = messageTemplateDao;
    }



    @RequestMapping("/test")
    private String test() {
        System.out.println("sout: Hello World");
        log.info("log: Hello World");
        return "Hello!";
    }
    @RequestMapping("/database")
    private String testDataBase() {
        List<MessageTemplate> list = messageTemplateDao.findAllByIsDeletedEquals(Byte.valueOf("0"), PageRequest.of(0, 10));
        return JSON.toJSONString(list);
    }
}
