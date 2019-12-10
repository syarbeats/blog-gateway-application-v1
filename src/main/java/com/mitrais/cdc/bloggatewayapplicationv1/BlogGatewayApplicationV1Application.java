/**
 * <h1>Application Gateway Main Class</h1>
 * This class is main class and entry point for gateway application.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

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
