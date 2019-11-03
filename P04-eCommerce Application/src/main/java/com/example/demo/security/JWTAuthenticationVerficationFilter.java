package com.example.demo.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationVerficationFilter  extends BasicAuthenticationFilter {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public JWTAuthenticationVerficationFilter(
            AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException{
        LOGGER.log(Level.INFO," JWTAuthenticationVerficationFilter-->doFilterInternal-->req: "+req );
        String header = req.getHeader(SecurityConstants.HEADER_STRING);
        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req,res);
        LOGGER.log(Level.INFO," JWTAuthenticationVerficationFilter-->doFilterInternal-->res: "+res );

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req){
        LOGGER.log(Level.INFO," JWTAuthenticationVerficationFilter-->doFilterInternal-->req: "+req );

        String token = req.getHeader(SecurityConstants.HEADER_STRING);
        if(token != null){
            String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if(user != null){
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
