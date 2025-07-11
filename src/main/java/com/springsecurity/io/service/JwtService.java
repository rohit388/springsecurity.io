package com.springsecurity.io.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class    JwtService {

/*
        private String SECRET_KEY= "asdfghjkl";

        We give the hardcode value (SECRET_KEY) that is the resone we go this error

        The specified key byte array is 56 bits which is not secure enough for any JWT HMAC-SHA algorithm.
        The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms
        MUST have a size >= 256 bits (the key size must be greater than or equal to the hash output size).
        Consider using the Jwts.SIG.HS256.key() builder (or HS384.key() or HS512.key())
        to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.
        See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.

*/


    private String SECRET_KEY= "";

    public JwtService() throws NoSuchAlgorithmException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            SECRET_KEY= Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();

    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
