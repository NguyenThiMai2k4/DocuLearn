package com.doculearn.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

// cầu nối xác thực đến /lti/lauth .
// giup TP và TC có thể verify thong tin cua nhau

@RestController
@RequestMapping("/oidc")
public class OidcController {

//    @Value("${ngrok.url}")
//    private String ngrokUrl;

    @PostMapping("/login")
    public ResponseEntity<String> oidcLogin(
            @RequestParam("iss") String issuer,
            @RequestParam("login_hint") String loginHint,
            @RequestParam("target_link_uri") String targetLinkUri,
            @RequestParam(value = "lti_message_hint", required = false) String ltiMessageHint,
            @RequestParam(value = "lti_deployment_id", required = false) String dpId) {

        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();



        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://moodle.drstrange.org/mod/lti/auth.php")
                .queryParam("response_type", "id_token")
                .queryParam("response_mode", "form_post")
                .queryParam("id_token_signed_response_alg", "RS256")
                .queryParam("scope", "openid")
                .queryParam("prompt", "none")
                .queryParam("client_id", "J7G08QdQTKnexlI") // clientId từ LMS
                //.queryParam("redirect_uri", ngrokUrl + "/doculearn/lti/launch")
                .queryParam("login_hint", loginHint)
                .queryParam("state", state)
                .queryParam("nonce", nonce);

        try {
            String encodedHint = URLEncoder.encode(ltiMessageHint, StandardCharsets.UTF_8.toString());
            builder.queryParam("lti_message_hint", encodedHint);
            builder.queryParam("lti_deployment_id", dpId);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

//        if (ltiMessageHint != null) {
//            builder.queryParam("lti_message_hint", ltiMessageHint);
//        }

        String redirectUrl = builder.build(true).toUriString();
        System.out.println("Redirect URL = " + redirectUrl);

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .body("Redirecting to: " + redirectUrl);
    }

}

