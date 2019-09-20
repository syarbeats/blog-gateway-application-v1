package com.mitrais.cdc.bloggatewayapplicationv1.config;

import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public EmailUtility emailUtility(){
        return new EmailUtility();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
