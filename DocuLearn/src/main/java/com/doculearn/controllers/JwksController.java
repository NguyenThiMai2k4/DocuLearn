package com.doculearn.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// return public key cuar TP cho LMS verify khi gui nguoc request
@RestController
@RequestMapping("/jwks")
public class JwksController {

    @GetMapping
    public Map<String, Object> jwks(HttpServletRequest req) {
        System.out.println("JWKS requested from: " + req.getRemoteAddr());
        Map<String, Object> jwk = new HashMap<>();
        jwk.put("kty", "RSA");
        jwk.put("use", "sig");
        jwk.put("alg", "RS256");
        jwk.put("kid", "tool-doculearn-1");
        jwk.put("n", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30"); // modulus base64url
        jwk.put("e", "AQAB");

        return Map.of("keys", List.of(jwk));
    }
}