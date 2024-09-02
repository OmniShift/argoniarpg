package com.kingargaroth.argoniarpg.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Enemy {
    public Enemy(String name, int maxHp, int attack, int defense) {
        this.name = name;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.attack = attack;
        this.defense = defense;
    }

    private final String name;
    @Setter
    private int hp;
    private final int maxHp;
    private final int attack;
    private final int defense;
}
