package com.kingargaroth.argoniarpg.entities.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnemyTemplate {
    ANGRY_TEENY("angry Teeny", 1, 100, 3, 1),
    BLACK_DRAGON("black dragon", 10, 1000, 10, 15),
    BLUE_DRAGON("blue dragon", 20, 2000, 20, 20),
    RED_DRAGON("red dragon", 40, 3500, 55, 30),
    GOLD_DRAGON("gold dragon", 45, 5000, 50, 40);

    // TODO add logic for weight by number of user characters

    private final String name;

    private final int level;

    private final int maxHp;

    private final int attack;

    private final int defense;
}
