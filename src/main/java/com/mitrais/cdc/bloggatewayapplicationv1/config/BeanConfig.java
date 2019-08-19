package com.mitrais.cdc.bloggatewayapplicationv1.config;

import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public EmailUtility emailUtility(){
        return new EmailUtility();
    }

}
