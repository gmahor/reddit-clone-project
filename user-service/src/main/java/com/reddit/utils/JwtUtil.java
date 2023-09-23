package com.reddit.utils;

import com.reddit.configs.EnvConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private EnvConfiguration envConfiguration;

    private PrivateKey privateKey;


    public String getToken(Object id, Object roles, String email) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        long expirationTime = envConfiguration.getAccessTokenExpiryTime();
        Key key = key();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("roles", roles);
        return buildToken(claims, email, expirationTime, key);
    }

    private String buildToken(Map<String, Object> claims, String email, long expirationTime, Key key) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expirationTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(envConfiguration.getJwtKeyPath()));
    }

}
