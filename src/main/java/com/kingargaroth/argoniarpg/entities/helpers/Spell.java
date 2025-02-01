package com.kingargaroth.argoniarpg.entities.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@AllArgsConstructor
@Getter
public enum Spell {
    FIREBALL("fireball", null),
    HEAL("heal", null),
    SUMMON_SKELETON("summon skeleton", "adding {effectStrength} skeletons to the fray");

    private final String name;
    private final String effectString;

    public static Spell getRandom() {
        return values()[new Random().nextInt(values().length)];
    }
}
