package com.miniprojet.wsrest.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Service
public class JwtService {

    private final String SECRET_KEY = "ASdfn&ASDFh89asd---TLW---sadf87234njkasdf89ASD";
    private final Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    private final long ACCESS_TOKEN_EXP = 1000 * 60 * 15;

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}