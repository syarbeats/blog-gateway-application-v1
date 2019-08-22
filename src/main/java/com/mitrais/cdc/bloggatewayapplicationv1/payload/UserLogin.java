package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

    private String username;
    private String password;

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLogin(){}
}
