package com.boat.mpp.web.config;

import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * swagger配置类
 *
 * @author boat
 */
@Component
@EnableOpenApi
@ApiModel
public class SwaggerConfiguration {
    
    /**
     * 对C端用户的接口文档
     * <p>
     * 地址：<a href="http://localhost:8888/swagger-ui/index.html">...</a>
     *
     * @return
     */
    @Bean
    public Docket webApiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("ClientInterfaceDocumentation")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.boat.mpp.web.controller"))
                //正则匹配请求路径，并分配到当前项目组
                //.paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("消息推送平台")
                .description("消息推送平台后端接口文档")
                .contact(new Contact("木舟", "http://github.com/boathermit", "yinzihang02@gmail.com"))
                .version("v1.0")
                .build();
    }

}