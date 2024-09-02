package com.kingargaroth.argoniarpg.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
public class CreateCharacterRequest {
    private String name;

    // Stats
    @Range(min = 0, max = 100)
    private int attack;
    @Range(min = 0, max = 100)
    private int defense;
}
