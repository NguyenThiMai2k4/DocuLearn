package com.doculearn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void printKeyParts() throws Exception {
        // Public key dạng Base64 (lấy từ PEM, bỏ phần header/footer)
        String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A...";

        byte[] decoded = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        RSAPublicKey rsaPub = (RSAPublicKey) publicKey;

        String n = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(rsaPub.getModulus().toByteArray());
        String e = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(rsaPub.getPublicExponent().toByteArray());

        System.out.println("n = " + n);
        System.out.println("e = " + e);
    }

}
