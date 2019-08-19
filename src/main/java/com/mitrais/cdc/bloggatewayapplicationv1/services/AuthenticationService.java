package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationProvider authenticationProvider;

    public boolean Login(AuthenticationPayload user){

        String username = user.getUsername();
        String password = user.getPassword();

        if(username != null && password != null ){

            try {
                Authentication authenticate = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            }catch (DisabledException e) {
                return false;
            }catch (BadCredentialsException e) {
                return false;
            }catch(InternalAuthenticationServiceException e) {
                return false;
            }
            catch(Exception e) {
                return false;
            }
        }

        return true;
    }
}
