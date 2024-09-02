package com.kingargaroth.argoniarpg.entities;

import com.kingargaroth.argoniarpg.converters.FeatConverter;
import com.kingargaroth.argoniarpg.converters.SpellConverter;
import com.kingargaroth.argoniarpg.request.CreateCharacterRequest;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Validated
@Entity
@Table(name = "userCharacter")
public class UserCharacter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1726599397483674385L;

    @Id
    @GeneratedValue
    private long characterId;

    @NotBlank
    private String twitchUsername;

    private String name;

    // Field is called "job" instead of "class", since the latter is a protected word in Java and cannot be used. We can still call it "class" in the frontend, though
    private Job job;

    @Range(min = 1, max = 100)
    private int level = 1;

    @Min(1)
    private int maxHp = 50;

    @Range(min = 1, max = 100)
    private int attack;

    @Range(min = 0, max = 100)
    private int defense;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipmentId")
    private List<Equipment> equipment = new ArrayList<>();

    @Convert(converter = SpellConverter.class)
    @ElementCollection(targetClass = Spell.class)
    @Enumerated(EnumType.STRING)
    private List<Spell> spells = new ArrayList<>();

    @Convert(converter = FeatConverter.class)
    @ElementCollection(targetClass = Feat.class)
    @Enumerated(EnumType.STRING)
    private List<Feat> feats = new ArrayList<>();

    @Min(0)
    private int gold;

    public UserCharacter(String twitchUsername, CreateCharacterRequest request) {
        this.twitchUsername = twitchUsername;
        this.name = request.getName();
        this.attack = request.getAttack();
        this.defense = request.getDefense();
        spells.add(Spell.getRandom());
        feats.add(Feat.getRandom());
    }

    public String toString() {
        // Exclude fields that have joins with other tables
        return "Character{characterId=%s, twitchUsername=%s, name=%s, level=%s, attack=%s, defense=%s, spells=%s, feat=%s, gold=%s}"
                .formatted(characterId, twitchUsername, name, level, attack, defense, spells, feats, gold);
    }
}
