package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationPayloadV1 {

    private boolean success;
    private String message;
    private UserLogin data;
}