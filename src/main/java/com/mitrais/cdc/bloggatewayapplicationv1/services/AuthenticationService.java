package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.LoginResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.TokenPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;


@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(AuthenticationPayload user){

        String username = user.getUsername();
        String password = user.getPassword();
        String token;
        User userLogin = userRepository.findByUsername(username);

        if(username != null && password != null ){

            if(userLogin != null){

                try {
                    Authentication authenticate = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                }catch (DisabledException e) {
                    throw new DisabledException("Account is disabled");
                }catch (BadCredentialsException e) {
                    throw new BadCredentialsException("Bad Credentials");
                }catch(InternalAuthenticationServiceException e) {
                    throw new InternalAuthenticationServiceException("Internal Authentication Service Exception");
                }
                catch (UsernameNotFoundException e){
                    throw new UsernameNotFoundException("Username not found");
                }
                catch(HttpServerErrorException e) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

        }

        List<String> roles = userLogin.getRoles();
        for(String role : roles){
            log.info("role--:"+ role);
        }

        token = jwtTokenProvider.createToken(username, roles);
        log.info("TOKEN:", token);

        return new LoginResponse(true, "You have login successfully", new TokenPayload(username, token));
    }
}
