package com.example.springlineauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;


@Service
public class AuthenticationService {

    @Value("${line.client_id}")
    private String clientId;

    @Value("${line.client_secret}")
    private String clientSecret;

    @Value("${line.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public String createAuthRedirectUrl() {
        return "https://access.line.me/oauth2/v2.1/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=random_state_string"
                + "&scope=openid%20profile";
    }

    /**
     * アクセストークンを取得する
     */
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.line.me/oauth2/v2.1/token", request, Map.class
        );

        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
    }
}
