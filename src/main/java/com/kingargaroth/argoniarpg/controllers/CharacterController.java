package com.kingargaroth.argoniarpg.controllers;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.entities.Enemy;
import com.kingargaroth.argoniarpg.entities.Spell;
import com.kingargaroth.argoniarpg.repositories.CharacterRepository;
import com.kingargaroth.argoniarpg.request.CreateCharacterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/character")
@Slf4j
public class CharacterController {

    @Autowired
    private CharacterRepository characterRepository;

    private Enemy activeEnemy;
    private List<UserCharacter> activeCombatants = new ArrayList<>();

    @PostMapping
    public ResponseEntity<UserCharacter> createCharacter(@RequestHeader("Twitch-Username") String twitchUser,
                                                         @Valid @RequestBody CreateCharacterRequest request) {
        UserCharacter userCharacter = new UserCharacter(twitchUser, request);
        UserCharacter userCharacterDB = characterRepository.save(userCharacter);
        log.debug("Created %s".formatted(userCharacterDB.toString()));
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }

    @PutMapping("/spell/add/{spellValue}")
    public ResponseEntity<UserCharacter> addSpell(@RequestHeader("Twitch-Username") String twitchUser,
                                                  @RequestHeader("Character-Name") String characterName,
                                                  @PathVariable String spellValue) throws Exception {
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUsernameAndName(twitchUser, characterName);
        if (userCharacter == null) {
            throw new Exception("Character for user %s not found".formatted(twitchUser));
        }
        Spell spell;
        if (spellValue.equalsIgnoreCase("random")) {
            List<Spell> selectableSpells = Arrays.stream(Spell.values()).filter(s ->
                    !userCharacter.getSpells().contains(s)).toList();
            if (selectableSpells.isEmpty()) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Error-Message", "Character already knows all spells");
                return new ResponseEntity<>(userCharacter, headers, HttpStatus.BAD_REQUEST);
            }
            spell = selectableSpells.get(new Random().nextInt(selectableSpells.size()));
        } else {
            spell = Spell.valueOf(spellValue.toUpperCase());
            if (userCharacter.getSpells().contains(spell)) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Error-Message", "Character already knows %s".formatted(spell.getName()));
                return new ResponseEntity<>(userCharacter, headers, HttpStatus.BAD_REQUEST);
            }
        }
        userCharacter.getSpells().add(spell);
        UserCharacter userCharacterDB = characterRepository.save(userCharacter);
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }

    @PostMapping("/combat")
    public ResponseEntity<Void> createCombat() {
        activeEnemy = new Enemy("Baby dragon", 100, 5, 0);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/combat/join")
    public ResponseEntity<String> joinCombat(@RequestHeader("Twitch-Username") String twitchUser,
                                             @RequestHeader("Character-Name") String characterName) throws Exception {
        if (characterName.equals("demo")) {

            if (activeEnemy == null) {
                activeEnemy = new Enemy("xX_ModHater69_Xx", 100, 5, 0);
            }
        }
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUsernameAndName(twitchUser, characterName);
        if (userCharacter == null) {
            throw new Exception("Character for user %s not found".formatted(twitchUser));
        }
        if (activeEnemy == null) {
            return new ResponseEntity<>("No enemy right now", HttpStatus.OK);
        }
        for (UserCharacter c : activeCombatants) {
            if (c.getCharacterId() == userCharacter.getCharacterId()) {
                return new ResponseEntity<>("%s already joined the fight".formatted(userCharacter.getName()), HttpStatus.OK);
            }
        }
        activeCombatants.add(userCharacter);
        return new ResponseEntity<>("%s joined the fight!".formatted(userCharacter.getName()), HttpStatus.OK);
    }

    @PostMapping("/combat/start")
    public ResponseEntity<String> startCombat() {
        if (activeEnemy == null) {
            return new ResponseEntity<>("No enemy right now", HttpStatus.OK);
        }
        for (int i = 0; i < 20; i++) {
            for (UserCharacter c : activeCombatants) {
                int damage = Math.max(0, c.getAttack() - activeEnemy.getDefense());
                activeEnemy.setHp(activeEnemy.getHp() - damage);
                if (activeEnemy.getHp() <= 0) {
                    activeEnemy = null;
                    return new ResponseEntity<>("%s was victorious over the enemy".formatted(activeCombatants.getFirst().getName()), HttpStatus.OK);
                }
            }
        }
        activeEnemy = null;
        activeCombatants = new ArrayList<>();
        return new ResponseEntity<>("%s could not be beaten".formatted(activeCombatants.getFirst().getName()), HttpStatus.OK);
    }
}
