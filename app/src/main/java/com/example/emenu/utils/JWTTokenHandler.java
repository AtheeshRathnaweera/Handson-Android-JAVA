package com.example.emenu.utils;

import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTTokenHandler {

    static String SIGN_IN_KEY = "secret";

    public static String createANew(String email) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, SIGN_IN_KEY.getBytes())
                .compact();
    }

    public static boolean validateJwt(String token, String email) {
        if (token != null && token.trim().length() > 0) {
            Claims claims = Jwts.parser()
                    .setSigningKey(SIGN_IN_KEY.getBytes())
                    .parseClaimsJws(token).getBody();

            Date date = claims.getExpiration();
            Object objectEmail = claims.get("email");

            return objectEmail != null && objectEmail.toString().equals(email) && new Date().compareTo(date) < 0;
        } else {
            return false;
        }
    }
}
