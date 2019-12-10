/**
 * <h1>Utility</h1>
 * This Class will be used to create helper function for
 * gateway project.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.utility;

import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResponseWrapper;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class Utility {

    private String message;
    private Object data;

    @Autowired
    private UserRepository userRepository;


    public Utility(String message, Object data){
        this.message = message;
        this.data = data;

    }

    public Utility(){}

    public ResponseWrapper getResponseData(){
        return new ResponseWrapper(this.message, this.data);
    }


}