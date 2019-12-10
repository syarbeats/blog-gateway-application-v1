/**
 * <h1>UserContext</h1>
 * Class to save all connection data.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.utility;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private String authToken= new String();

    /**
     * This method will be used to get token
     * for internal service invocation purpose.
     *
     * @return will return token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * This method will be used to save token
     * in user context holder.
     *
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}