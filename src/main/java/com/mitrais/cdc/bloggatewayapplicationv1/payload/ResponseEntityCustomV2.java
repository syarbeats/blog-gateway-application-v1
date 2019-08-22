package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEntityCustomV2 {
    private Contents contents;
    private String message;
}
