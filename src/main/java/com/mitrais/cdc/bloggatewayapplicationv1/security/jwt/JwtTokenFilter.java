/**
 * <h1>JWTTokenFilter</h1>
 * Class to set JwtTokenProvider into JWTTokenFilter
 * and To intercept Request before invoke controller.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */


package com.mitrais.cdc.bloggatewayapplicationv1.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {


    private JwtTokenProvider jwtTokenProvider;
    
    private Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);


    /**
     * This constructor method will used to setup JwtTokenProvider Object
     *
     * @param //jwtTokenProvider
     */
   /* public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }*/
    public JwtTokenFilter() {

    }

    /**
     * This method will be used to intercept request
     * to take Token in its request header and then
     * validate it and set Authentication in SecurityContextHolder.
     *
     * @param req
     * @param res
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            if (auth != null) {
            	logger.info("Authorized Token: "+token);
            	logger.info("Authorized Username: "+ this.jwtTokenProvider.getUsername(token));
                                                   
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(req, res);
        
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


}
