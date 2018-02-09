package com.sample.messagingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

    @Value("${info.build.version}")
    private String buildVersion;

    @Value("${info.build.number}")
    private String buildNumber;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.storytel.assignment.messagingservice.controller")).build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        String version = buildVersion;
        if (!"LOCAL".equals(buildNumber)) {
            version += "-" + buildNumber;
        }

        return new ApiInfoBuilder().title("Messaging Service")
                .description("Messaging service for creating, modifying, deleting and retrieving messages.").version(version).build();
    }

}
