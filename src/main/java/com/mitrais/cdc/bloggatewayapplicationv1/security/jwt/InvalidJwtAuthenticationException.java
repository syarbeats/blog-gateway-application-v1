package com.mitrais.cdc.bloggatewayapplicationv1.security.jwt;

/**
 * Created by Syarif Hidayat on 22/04/2019.
 */


import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJwtAuthenticationException(String e) {
    	super(e);
    }
}
