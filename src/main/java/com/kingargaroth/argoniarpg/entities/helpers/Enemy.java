package com.kingargaroth.argoniarpg.entities.helpers;

import com.kingargaroth.argoniarpg.entities.Combatant;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Enemy extends Combatant {

    public Enemy(EnemyTemplate template) {
        this.setName(template.getName());
        this.setLevel(template.getLevel());
        this.setHp(template.getMaxHp());
        this.setMaxHp(template.getMaxHp());
        this.setAttack(template.getAttack());
        this.setDefense(template.getDefense());
    }
}
