package com.api.project.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class ZoomTokenGenerator {

    private static final String API_KEY = "o15hqOHQSJao9PpU1SAspg"; // Replace with your API Key
    private static final String API_SECRET = "T1kqeUI4425SGiR5IQeUCSGQsvkZ3pSK"; // Replace with your API Secret

    public static String generateToken() {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // 1 hour expiration
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setIssuer(API_KEY)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, API_SECRET.getBytes())
                .compact();
    }

    public static void main(String[] args) {
        String token = generateToken();
        System.out.println("Generated JWT Token: " + token);
    }
}
