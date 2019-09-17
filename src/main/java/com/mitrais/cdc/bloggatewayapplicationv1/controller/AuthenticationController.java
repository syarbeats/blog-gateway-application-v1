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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController extends CrossOriginController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/authentication")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationPayload authenticationPayload){

        LoginResponse response = authenticationService.login(authenticationPayload);
        return ResponseEntity.ok(response);
    }
}
