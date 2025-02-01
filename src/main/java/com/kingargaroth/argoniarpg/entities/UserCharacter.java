package com.kingargaroth.argoniarpg.entities;

import com.kingargaroth.argoniarpg.converters.FeatConverter;
import com.kingargaroth.argoniarpg.converters.SpellConverter;
import com.kingargaroth.argoniarpg.entities.helpers.Feat;
import com.kingargaroth.argoniarpg.entities.helpers.Job;
import com.kingargaroth.argoniarpg.entities.helpers.Spell;
import com.kingargaroth.argoniarpg.request.CreateCharacterRequest;
import com.kingargaroth.argoniarpg.response.UserCharacterDto;
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
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Validated
@Entity
@Table(name = "userCharacter")
public class UserCharacter extends Combatant {

    @Id
    @GeneratedValue
    private long characterId;

    @NotBlank
    private String twitchUserId;

    // Field is called "job" instead of "class", since the latter is a protected word in Java and cannot be used. We can still call it "class" in the frontend, though
    @Enumerated(EnumType.STRING)
    private Job job;

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

    public UserCharacter(String twitchUserId, CreateCharacterRequest request) {
        this.twitchUserId = twitchUserId;
        this.setName(request.getName());
        this.setAttack(request.getAttack());
        this.setDefense(request.getDefense());
        spells.add(Spell.getRandom());
        feats.add(Feat.getRandom());
    }

    public UserCharacterDto toDto() {
        return new UserCharacterDto(this.getName(), this.getJob(), this.getEquipment(), this.getSpells(),
                this.getFeats(), this.getLevel(), this.getMaxHp(), this.getAttack(), this.getDefense());
    }

    public String toString() {
        // Exclude fields that have joins with other tables
        return "Character{characterId=%s, twitchUsername=%s, name=%s, level=%s, attack=%s, defense=%s, spells=%s, feat=%s, gold=%s}"
                .formatted(characterId, twitchUserId, this.getName(), this.getLevel(), this.getAttack(), this.getDefense(), spells, feats, gold);
    }
}
