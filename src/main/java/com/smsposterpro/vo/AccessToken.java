package com.smsposterpro.vo;

import lombok.Data;

@Data
public class AccessToken {
    //access_token=39_JGOWTdIgQM6F_bB1cgEC3VcdRYJ61Bi-Q6FJ8AB7LElhWY1z3BlRC6U0iTcqMOAlG3IFr47KESeNeKlAsbLbCRhIssWxTBGM5pDiys9ooQAikHF64n3siKDRxJy1jX3gguFJeyt-V2ddy-3gVWIeAIAJKW,
    //expires_in=7200
    private String access_token;
    private String expires_in;
    private Long currentTime = 0L;
}
