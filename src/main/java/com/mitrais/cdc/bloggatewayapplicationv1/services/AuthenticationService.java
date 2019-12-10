/**
 * <h1>Authentication Service</h1>
 * Class to handle Authentication Process .
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.LoginResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.TokenPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;


@Service
@Slf4j
public class AuthenticationService {


    private UserRepository userRepository;
    private AuthenticationProvider authenticationProvider;
    private JwtTokenProvider jwtTokenProvider;

    /**
     * This Constructor method will be used to setup UserRepository,
     * AuthenticationProvider and JwtTokenProvider as well.
     *
     * @param userRepository
     * @param authenticationProvider
     * @param jwtTokenProvider
     */
    public AuthenticationService(UserRepository userRepository, AuthenticationProvider authenticationProvider, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * This method will be used to authenticate
     * username and password in login process.
     *
     * @param user
     * @return will return username and token
     */
    public LoginResponse login(@Nonnull AuthenticationPayload user){

        @CheckForNull
        String username = user.getUsername();
        @CheckForNull
        String password = user.getPassword();
        String token;

        @CheckForNull
        User userLogin = userRepository.findByUsername(username);

        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
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

        @CheckForNull
        List<String> roles = userLogin.getRoles();
        for(String role : roles){
            log.info("role--:"+ role);
        }

        token = jwtTokenProvider.createToken(username, roles);
        log.info("TOKEN:", token);

        return new LoginResponse(true, "You have login successfully", new TokenPayload(username, token));
    }
}
