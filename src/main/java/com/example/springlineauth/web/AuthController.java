package com.example.springlineauth.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Value("${line.channel-id}")
    private String channelId;

    @Value("${line.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login")
    public String login() {
        String lineLoginUrl = "https://access.line.me/oauth2/v2.1/authorize?response_type=code"
                + "&client_id=" + channelId
                + "&redirect_uri=" + redirectUri
                + "&state=random_state_string"
                + "&scope=openid%20profile";
        return "redirect:" + lineLoginUrl;
    }

    @GetMapping("/login/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        // TODO: ここでLINEのトークンエンドポイントにリクエストを送信し、アクセストークンを取得
        System.out.println("Authorization Code: " + code);
        return "auth/callback";
    }

}
