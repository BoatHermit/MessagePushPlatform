package com.boat.mpp.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Boat
 */
@RestController
@Slf4j
public class TestController {
    @RequestMapping("/test")
    private String test() {
        System.out.println("sout: Hello World");
        log.info("log: Hello World");
        return "Hello!";
    }
}
