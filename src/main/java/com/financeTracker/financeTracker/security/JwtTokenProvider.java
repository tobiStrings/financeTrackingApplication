package com.financeTracker.financeTracker.security;

import com.financeTracker.financeTracker.data.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.SSLConf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyStore;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtTokenProvider {
    @Value("${jwt.secrete}")
    private String jwtSecrete;
    @Value("${jwt.expiration}")
    private long jwtExpiration;



    public String getUsernameFromJwtToken(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] KeyBytes = Decoders.BASE64.decode(jwtSecrete);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractClaims(token);

        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username  = getUsernameFromJwtToken(jwt);
        return username.equals(userDetails.getUsername()) && isTokenExpired(jwt);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).after(new Date());
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims,UserDetails userDetails){
        return Jwts.builder().setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
    }
}
