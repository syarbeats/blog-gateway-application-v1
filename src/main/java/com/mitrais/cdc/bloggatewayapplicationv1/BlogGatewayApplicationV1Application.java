package com.mitrais.cdc.bloggatewayapplicationv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@RefreshScope
public class BlogGatewayApplicationV1Application {

    public static void main(String[] args) {
        SpringApplication.run(BlogGatewayApplicationV1Application.class, args);
    }

}
