package com.reddit.utils;


import com.reddit.config.EnvConfiguration;
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
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final EnvConfiguration envConfiguration;

    private PublicKey publicKey;

    @Autowired
    public JwtUtil(EnvConfiguration envConfiguration) {
        this.envConfiguration = envConfiguration;
    }


    public boolean validateToken(Claims claims) {
        if (claims == null) {
            return false;
        } else {
            return claims.getExpiration().after(new Date());
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Exception in token verification::Token:: {}, {}", token, e.getMessage());
        }
        return null;
    }

//    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
//        if (this.publicKey == null) {
//            String publicKeyPEM = new String(Files.readAllBytes(Paths.get(envConfiguration.getJwtKeyPath())), StandardCharsets.UTF_8);
//            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
//            publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
//            publicKeyPEM = publicKeyPEM.replaceAll("[\n|\r]", "");
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM));
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            this.publicKey = kf.generatePublic(keySpec);
//        }
//        return this.publicKey;
//    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(envConfiguration.getJwtKeyPath()));
    }
}
