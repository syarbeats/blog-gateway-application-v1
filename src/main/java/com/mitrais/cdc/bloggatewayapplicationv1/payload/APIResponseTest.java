package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponseTest {

    private boolean success;
    private String message;
    private User data;

    public APIResponseTest(boolean success, String message, User data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
