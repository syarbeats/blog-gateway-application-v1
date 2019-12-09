package com.mitrais.cdc.bloggatewayapplicationv1.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));

        if(httpServletRequest.getHeader("Authorization") != null){
            UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader("Authorization").split(" ")[1]);
        }

        UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));
        log.info("AUTH-TOKEN {} ", UserContextHolder.getContext().getAuthToken());
        chain.doFilter(httpServletRequest, response);
    }

    @Override
    public void destroy() {

    }
}
