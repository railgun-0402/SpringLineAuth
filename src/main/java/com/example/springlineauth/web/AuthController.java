package com.example.springlineauth.web;

import com.example.springlineauth.service.AuthenticationService;
import com.example.springlineauth.user.LineUserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class AuthController {

    private static final Logger LOG = LogManager.getLogger(AuthController.class.getName());

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public RedirectView login(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            LOG.info("既にこのユーザはログイン済みです: {}", userDetails.getUsername());
            return new RedirectView("/");
        }

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
        // TODO: 受け取ったcodeやstateを使用してセッション照合を実施したい
        try {
            // アクセストークン取得
            String accessToken = authenticationService.getAccessToken(code);
            System.out.println("アクセストークンが取れたよ: " + accessToken);

            LineUserProfile profile = authenticationService.getUserProfile(accessToken);
            System.out.println("ユーザプロファイルが取れたよ: " + profile);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(profile.getUserId(), null, List.of());

            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Authorization Code: " + code);

            // ログイン成功時は認証後ページへ
            return "auth/callback";

        } catch (Exception e) {
            // 認証失敗時
            return "redirect:/login?error=true";
        }
    }
}
