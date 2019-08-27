package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.LoginResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.TokenPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
                    return new LoginResponse(false, "Disabled Exception", null);
                }catch (BadCredentialsException e) {
                    return new LoginResponse(false, "Bad Credentials Exception", null);
                }catch(InternalAuthenticationServiceException e) {
                    return new LoginResponse(false, "Internal Authentication Service Exception", null);
                }
                catch(Exception e) {
                    return new LoginResponse(false, "Exception", null);
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
