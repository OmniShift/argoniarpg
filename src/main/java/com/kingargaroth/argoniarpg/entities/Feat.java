package com.kingargaroth.argoniarpg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@AllArgsConstructor
@Getter
public enum Feat {
    BRAWLER("brawler"),
    SENTINEL("sentinel"),
    COMBAT_MAGE("combat mage");

    private final String name;

    public static Feat getRandom() {
        return values()[new Random().nextInt(values().length)];
    }
}
