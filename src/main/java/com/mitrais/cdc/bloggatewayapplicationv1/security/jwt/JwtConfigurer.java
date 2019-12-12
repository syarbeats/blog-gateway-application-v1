/**
 * <h1>JWTConfigurer </h1>
 * Class to set JwtTokenProvider into JWTTokenFilter
 * and To intercept Request before invoke controller.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */


package com.mitrais.cdc.bloggatewayapplicationv1.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;

    private JwtTokenFilter jwtTokenFilter;

    /**
     * This constructor method will be used to setup JwtTokenProvider for
     * JwtConfigurer.
     *
     * @param //jwtTokenProvider
     */
    /*public JwtConfigurer(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }*/
    public JwtConfigurer() {

    }

    /**
     * This method will be used to add custom filter
     * to intercept request before call controller.
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void setJwtTokenFilter(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }
}
