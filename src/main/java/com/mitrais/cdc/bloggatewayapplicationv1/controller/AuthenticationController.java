package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.AuthenticationPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.LoginResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.TokenPayload;
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
    public ResponseEntity<?> Login(@RequestBody AuthenticationPayload authenticationPayload){

        Map<Object, Object> data = new HashMap<>();
        LoginResponse response = authenticationService.Login(authenticationPayload);
        String token = response.getData().getToken();
        String username = response.getData().getUsername();

        if(!response.isSuccess()){
            data.put("status", false);
            data.put("message", "Authentication is failed");
            data.put("token", "");
            return ResponseEntity.ok(data);
        }
        data.put("username", username);
        data.put("status", true);
        data.put("message", "You have login successfully");
        data.put("token", token);

        return ResponseEntity.ok(data);
    }
}
