package com.example.springlineauth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Reflectionを使って private フィールドを設定
        authenticationService = new AuthenticationService();
        setPrivateField(authenticationService, "clientId", "mock_client_id");
        setPrivateField(authenticationService, "redirectUri", "https://test.com/callback");
    }

    /**
     * privateフィールドに値をセットするヘルパーメソッド
     * @param target インスタンス
     * @param fieldName 設定値
     * @param value 実際にセットする値
     */
    private void setPrivateField(Object target, String fieldName, String value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * リダイレクトURLの値が正しく作成されること
     */
    @Test
    void createAuthRedirectUrl_ShouldGenerateValidUrl() {
        // テスト対象メソッドの実行
        String url = authenticationService.createAuthRedirectUrl();

        // 検証
        assertTrue(url.contains("https://access.line.me/oauth2/v2.1/authorize"));
        assertTrue(url.contains("response_type=code"));
        assertTrue(url.contains("client_id=mock_client_id"));
        assertTrue(url.contains("redirect_uri=https://test.com/callback"));
        assertTrue(url.contains("scope=openid%20profile"));
    }
}