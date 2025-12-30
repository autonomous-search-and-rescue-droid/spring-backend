package com.cosmicbook.search_and_rescue_droid.security;

import com.cosmicbook.search_and_rescue_droid.model.Role;
import com.cosmicbook.search_and_rescue_droid.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${cosmicbook.jwtSecret}")
    private String jwtSecret;

    @Value("${cosmicbook.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("roles", user.getRole().stream().map(Role::getName).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            System.err.println("JWT Error : " + e.getMessage());
        }
        return false;
    }

}
