package com.boat.mpp.web.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * 通用配置
 */
@Configuration
public class CommonConfiguration {

    /**
     * FastJson 消息转换器 格式化输出json
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = Lists.newArrayList();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        return new HttpMessageConverters(fastConverter);
    }
}
