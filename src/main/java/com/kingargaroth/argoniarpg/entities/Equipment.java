package com.kingargaroth.argoniarpg.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@Setter
@Validated
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {

    @Serial
    private static final long serialVersionUID = -8292821874136179988L;

    @Id
    @GeneratedValue
    private long equipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characterId")
    private UserCharacter owner;

    private final String name;

    @Range(min = 0, max = 10)
    private int level;

    private final Type type;

    private int attackMod;

    private int defenseMod;

    public String toString() {
        return "Equipment{equipmentId=%s, ownerId=%s, name=%s}".formatted(equipmentId, owner.getCharacterId(), name);
    }

    @AllArgsConstructor
    @Getter
    private enum Type {
        // Weapons
        SWORD("sword"),
        DAGGER("dagger"),
        STAFF("staff"),
        // Armor
        HELMET("helmet"),
        CHEST("chestpiece"),
        GLOVES("gloves"),
        LEGGINGS("leggings"),
        BOOTS("boots"),
        // Accessories
        RING("ring"),
        NECKLACE("necklace");

        private final String name;
    }
}
