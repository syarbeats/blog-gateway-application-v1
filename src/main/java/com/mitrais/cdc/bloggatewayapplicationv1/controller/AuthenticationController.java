package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/authentication")
    public ResponseEntity<?> Login(@RequestBody AuthenticationPayload authenticationPayload){

        if(!authenticationService.Login(authenticationPayload)){
            return ResponseEntity.ok("Authentication is failed");
        }
        return ResponseEntity.ok("Authentication is success");
    }
}
