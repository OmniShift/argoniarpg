package com.kingargaroth.argoniarpg.response;

import com.kingargaroth.argoniarpg.converters.FeatConverter;
import com.kingargaroth.argoniarpg.converters.SpellConverter;
import com.kingargaroth.argoniarpg.entities.Equipment;
import com.kingargaroth.argoniarpg.entities.helpers.Feat;
import com.kingargaroth.argoniarpg.entities.helpers.Job;
import com.kingargaroth.argoniarpg.entities.helpers.Spell;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UserCharacterDto {

    private String characterName;

    // Field is called "job" instead of "class", since the latter is a protected word in Java and cannot be used. We can still call it "class" in the frontend, though
    private Job job;

    private List<Equipment> equipment = new ArrayList<>();

    @Convert(converter = SpellConverter.class)
    private List<Spell> spells = new ArrayList<>();

    @Convert(converter = FeatConverter.class)
    private List<Feat> feats = new ArrayList<>();

    private int level;

    private int maxHp;

    private int attack;

    private int defense;
}
