/**
 * <h1>Authentication Controller</h1>
 * Class to create API Controller for Authentication process
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.LoginResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

@RestController
@RequestMapping("/api")
public class AuthenticationController extends CrossOriginController {

    private AuthenticationService authenticationService;

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * This method will be used as entry point to the application
     * Successfully authentication process will return username and token.
     *
     * @param authenticationPayload
     * @return It will return username and token.
     */
    @PostMapping("/authentication")
    public ResponseEntity<LoginResponse> login(@RequestBody @Nonnull AuthenticationPayload authenticationPayload){

        LoginResponse response = getAuthenticationService().login(authenticationPayload);
        return ResponseEntity.ok(response);
    }
}
