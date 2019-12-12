/**
 * <h1>Bean Configuration</h1>
 * Class to create required bean
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.config;

import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenFilter;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenProvider;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class BeanConfig {

    /**
     * Create EmailUtility Bean
     * @return EmailUtility Bean
     */
    @Bean
    public EmailUtility emailUtility(){
        return new EmailUtility();
    }

    /**
     * Create BCryptPasswordEncoder to encrypt password
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
