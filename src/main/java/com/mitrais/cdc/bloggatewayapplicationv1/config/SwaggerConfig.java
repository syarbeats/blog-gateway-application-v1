package com.mitrais.cdc.bloggatewayapplicationv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(null, null, null, null, "", ApiKeyVehicle.HEADER,"Authorization","");
    }
	
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.mitrais.cdc.bloggatewayapplicationv1.controller"))
                .paths(regex("/api.*"))
                .build()
                .apiInfo(metaData())
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Mitrais - CDC Java Boot Camp",
                "Spring Boot REST API for  Gateway Application",
                "1.0",
                "Terms of service",
                new Contact("Mitrais", "www.mitrais.com", "cdc@mitrais.com"),
               "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }
    
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    } 
}