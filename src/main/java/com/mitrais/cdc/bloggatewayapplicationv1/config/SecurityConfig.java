/**
 * <h1>Security Configuration</h1>
 * Class to config spring security
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.config;

import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.mitrais.cdc.bloggatewayapplicationv1.services.UserDetailsServices;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenProvider;



@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserDetailsServices userDetailsService;

    private JwtConfigurer jwtConfigurer;

    /**
     * Constructor to set JwtTokenProvider, UserDetailServices and PasswordEncoder
     *
     * @param tokenProvider
     * @param userDetailsServices
     * @param passwordEncoder
     */
    public SecurityConfig(JwtTokenProvider tokenProvider, UserDetailsServices userDetailsServices, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsServices;
    }

    /**
     * This method will return AuthenticationProvider Object that will be used
     * to handle authentication and authorization in the project.
     * UserDetailsService and Password Encoder will be injected into this object.
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        log.info("Authentication Provider Process.....");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);

        return daoAuthenticationProvider;
    }

    /**
     * This method will be used to bypass spring security for certain url
     *
     * @param http
     * @throws Exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/activate*").permitAll()
                .antMatchers("/api/register*").permitAll()
                .antMatchers("/api/resetpassword*").permitAll()
                .antMatchers("/api/reset*").permitAll()
                .antMatchers("/api/authentication", "/actuator", "/actuator/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);


    }

    /**
     * This method will be used to inject JwtTokenProvider into JwtConfigurer Object.
     *
     * @return JwtConfigurer object
     */
    /*private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer();
    }*/

    @Autowired
    public void setJwtConfigurer(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }
}
