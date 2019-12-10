/**
 * <h1>JWTokenProvider</h1>
 * Class to handle Token creation and token validation as well .
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */


package com.mitrais.cdc.bloggatewayapplicationv1.security.jwt;

import com.mitrais.cdc.bloggatewayapplicationv1.services.UserDetailsServices;
import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {


    private String secretKey = "Ini Rahasia Choy";

    private long validityInMilliseconds = 86400000;

    @Autowired
    private UserDetailsServices userDetailsService;
    
    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * This method is executed after all bean have been initialized,
     * This method encode secret key using Base64 encoder.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * This method will be used to create token for validated username,
     * The Token will have expired when validity time has been succeeded.
     *
     * @param username
     * @param roles
     * @return will return token for validated username
     */
    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
            .setExpiration(validity)//
            .signWith(SignatureAlgorithm.HS256, secretKey)//
            .compact();
    }

    /**
     * This method will be used to extract username from token,
     * and then use this username to get user data and create
     * Authentication Object.
     *
     * @param token
     * @return will return authentication object
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * This method will be used to extract username from token.
     *
     * @param token
     * @return will return username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * This method will be used to take token
     * from request header
     *
     * @param req
     * @return will return jwt token
     */
    public String resolveToken(HttpServletRequest req) {
    
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * This method will be used to check token validity time,
     * True if validity time > current date
     * False if validity time < current date
     *
     * @param token
     * @return will return true if token has not expired yet
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            logger.info("Current login username:"+this.getUsername(token));

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}
