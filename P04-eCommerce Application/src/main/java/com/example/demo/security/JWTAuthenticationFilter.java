package com.example.demo.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final static Logger log = LoggerFactory.getLogger(JWTAuthenticationVerficationFilter.class);
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            HttpServletResponse res) throws AuthenticationException {
        try {
//            log.info("JWTAuthenticationFilter-->attemptAuthentication : {}",req);

            User credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);
//            log.info("JWTAuthenticationFilter-->attemptAuthentication-->credentials.username : {} password {}"
//                    ,credentials.getUsername(), credentials.getPassword());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication auth)
            throws IOException, ServletException {
//        log.info("JWTAuthenticationFilter-->successfulAuthentication-->req: {}",req);
        String token = JWT.create()
                .withSubject(
                        ((org.springframework.security.core.userdetails.User) auth.getPrincipal())
                                .getUsername())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
//        log.info("JWTAuthenticationFilter-->successfulAuthentication-->res:{} ", res );
    }

}
