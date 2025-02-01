package com.kingargaroth.argoniarpg.entities;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Validated
public abstract class Combatant implements Serializable {

    @Serial
    private static final long serialVersionUID = -7262934682838487721L;

    private String name;

    @Range(min = 1, max = 100)
    private int level = 1;

    @Transient
    private int hp;

    @Min(1)
    private int maxHp;

    @Range(min = 1, max = 100)
    private int attack;

    @Range(min = 0, max = 100)
    private int defense;
}
