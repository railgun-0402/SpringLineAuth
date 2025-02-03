package com.example.springlineauth.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LineUserProfile {
    private String userId;
    private String displayName;
    private String pictureUrl;
    private String statusMessage;

}
