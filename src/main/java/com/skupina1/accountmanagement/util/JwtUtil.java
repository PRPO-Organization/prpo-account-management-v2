package com.skupina1.accountmanagement.util;

import com.skupina1.accountmanagement.config.Config;
import com.skupina1.accountmanagement.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil{
    //private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String KEY_STRING = Config.getJWTSecret();
    private static final Key KEY = Keys.hmacShaKeyFor(KEY_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    public static String genToken(String email, String role){
        return Jwts.builder().setSubject(email).claim("role", role).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY).compact();
    }

    public static String getRoleFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody()
                .get("role", String.class);
    }

    public static String getEmailFromToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch(JwtException e){
            System.err.println(e);
            return null;
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static User parseToken(String token){
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);

            Claims claims = jws.getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            User user = new User();
            user.setEmail(email);
            user.setRole(role);

            return user;
        } catch (JwtException e) {
            System.err.println("Invalid token: " + e.getMessage());
            return null;
        }

    }
}
