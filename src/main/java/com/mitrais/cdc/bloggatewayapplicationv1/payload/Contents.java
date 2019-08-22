package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contents {

    private boolean success;
    private String message;
    private User data;
}
