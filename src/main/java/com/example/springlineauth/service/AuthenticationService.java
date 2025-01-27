package com.example.springlineauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Value("${line.client_id}")
    private String clientId;

    @Value("${line.redirect_uri}")
    private String redirectUri;

    public String createAuthRedirectUrl() {
        return "https://access.line.me/oauth2/v2.1/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=random_state_string"
                + "&scope=openid%20profile";
    }

}
