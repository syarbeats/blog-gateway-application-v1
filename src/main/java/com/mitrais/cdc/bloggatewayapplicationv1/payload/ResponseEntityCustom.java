package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEntityCustom {

    /*private Contents contents;*/
    private ContentsPayload contents;
    private String message;
}
