package com.kingargaroth.argoniarpg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@AllArgsConstructor
@Getter
public enum Spell {
    FIREBALL("fireball"),
    HEAL("heal"),
    SUMMON_SKELETON("summon skeleton");

    private final String name;

    public static Spell getRandom() {
        return values()[new Random().nextInt(values().length)];
    }
}
