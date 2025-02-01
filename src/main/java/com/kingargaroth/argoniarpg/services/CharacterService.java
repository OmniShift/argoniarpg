package com.kingargaroth.argoniarpg.services;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.entities.helpers.Spell;
import com.kingargaroth.argoniarpg.repositories.CharacterRepository;
import com.kingargaroth.argoniarpg.request.CharacterActionRequest;
import com.kingargaroth.argoniarpg.request.CreateCharacterRequest;
import com.kingargaroth.argoniarpg.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public ResponseEntity<UserCharacter> createCharacter(String twitchUserId, CreateCharacterRequest object) {
        UserCharacter userCharacterDB = characterRepository.findFirstByTwitchUserId(twitchUserId);
        if (userCharacterDB != null) {
            return ErrorCode.USER_HAS_CHARACTER.createErrorResponse(userCharacterDB);
        }
        UserCharacter userCharacter = new UserCharacter(twitchUserId, object);
        userCharacterDB = characterRepository.save(userCharacter);
        log.debug("Created %s".formatted(userCharacterDB.toString()));
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }

    public ResponseEntity<UserCharacter> addSpell(CharacterActionRequest object, String spellValue) {
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUserId(object.getTwitchUserId());
        if (userCharacter == null) {
            log.warn("Character for user %s not found".formatted(object.getTwitchUserId()));
            return ErrorCode.CHARACTER_NOT_FOUND.createErrorResponse(null, object.getTwitchUserId());
        }
        Spell spell;
        if (spellValue.equalsIgnoreCase("random")) {
            List<Spell> selectableSpells = Arrays.stream(Spell.values()).filter(s ->
                    !userCharacter.getSpells().contains(s)).toList();
            if (selectableSpells.isEmpty()) {
                return ErrorCode.ALL_SPELLS_KNOWN.createErrorResponse(userCharacter);
            }
            spell = selectableSpells.get(new Random().nextInt(selectableSpells.size()));
        } else {
            spell = Spell.valueOf(spellValue.toUpperCase());
            if (userCharacter.getSpells().contains(spell)) {
                return ErrorCode.SPELL_KNOWN.createErrorResponse(userCharacter, spell.getName());
            }
        }
        userCharacter.getSpells().add(spell);
        UserCharacter userCharacterDB = characterRepository.save(userCharacter);
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }
}
