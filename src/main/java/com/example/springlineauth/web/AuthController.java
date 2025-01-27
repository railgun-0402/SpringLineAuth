package com.example.springlineauth.web;

import com.example.springlineauth.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {

    private static final Logger LOG = LogManager.getLogger(AuthController.class.getName());

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public RedirectView login() {
        String redirectUrl = "";

        try {
            redirectUrl = authenticationService.createAuthRedirectUrl();
        } catch (NullPointerException e) {
            LOG.warn("redirectURLがNULLです: {}", e.getMessage());
            return new RedirectView("/", true);
        }

        LOG.debug("アカウント認証ページにリダイレクトします redirectUrl={}", redirectUrl);

        return new RedirectView(redirectUrl);
    }

    @GetMapping("/login/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        // TODO: ここでLINEのトークンエンドポイントにリクエストを送信し、アクセストークンを取得
        System.out.println("Authorization Code: " + code);
        return "auth/callback";
    }

}
