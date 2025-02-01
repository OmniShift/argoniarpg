package com.kingargaroth.argoniarpg.request;

import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
public class CharacterActionRequest {
    private String twitchUserId;
    private Long characterId;
}
