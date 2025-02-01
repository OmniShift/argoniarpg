package com.kingargaroth.argoniarpg.services;

import com.kingargaroth.argoniarpg.entities.Combat;
import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.entities.helpers.Enemy;
import com.kingargaroth.argoniarpg.entities.helpers.EnemyTemplate;
import com.kingargaroth.argoniarpg.repositories.CharacterRepository;
import com.kingargaroth.argoniarpg.repositories.CombatRepository;
import com.kingargaroth.argoniarpg.request.JoinCombatRequest;
import com.kingargaroth.argoniarpg.response.CombatResponse;
import com.kingargaroth.argoniarpg.response.ErrorCode;
import com.kingargaroth.argoniarpg.response.UserCharacterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Slf4j
public class CombatService {

    private Combat currentCombat = null;

    @Autowired
    private CombatRepository combatRepository;

    @Autowired
    private CharacterRepository characterRepository;

    public ResponseEntity<String> joinCombat(JoinCombatRequest object) {
        Combat dbCombat = combatRepository.findFirstOrderByCreatedTsDesc();
        if (dbCombat == null || dbCombat.getCombatState() == Combat.CombatState.VIEWED) {
            if (currentCombat == null) {
                currentCombat = new Combat();
                combatRepository.save(currentCombat);
            }
        } else if (dbCombat.getCombatState() == Combat.CombatState.CONCLUDED) {
            return ErrorCode.COMBAT_STARTED.createErrorResponse(null);
        }
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUserId(object.getTwitchUserId());
        if (userCharacter == null) {
            log.warn("Character for user %s not found".formatted(object.getTwitchUserId()));
            // TODO create a default character instead if that doesn't lock people into things?
            return ErrorCode.CHARACTER_NOT_FOUND.createErrorResponse(null, object.getTwitchUserId());
        }
        if (currentCombat.getCombatState() != Combat.CombatState.IN_PREPARATION) {
            return ErrorCode.NO_COMBAT.createErrorResponse(null);
        }
        for (UserCharacter c : currentCombat.getCombatants()) {
            if (c.getCharacterId() == userCharacter.getCharacterId()) {
                return ErrorCode.COMBAT_ALREADY_JOINED.createErrorResponse(null, userCharacter.getName());
            }
        }
        currentCombat.getCombatants().add(userCharacter);
        return new ResponseEntity<>("%s joined the fight!".formatted(userCharacter.getName()), HttpStatus.OK);
    }

    public ResponseEntity<CombatResponse> startCombat() {
        if (currentCombat == null) {
            return ErrorCode.NO_COMBAT.createErrorResponse(null);
        } else if (currentCombat.getCombatState() != Combat.CombatState.IN_PREPARATION) {
            return switch (currentCombat.getCombatState()) {
                case Combat.CombatState.CONCLUDED -> ErrorCode.COMBAT_STARTED.createErrorResponse(null);
                case Combat.CombatState.VIEWED -> ErrorCode.NO_COMBAT.createErrorResponse(null);
                default -> null;
            };
        }
        List<UserCharacter> combatants = currentCombat.getCombatants();
        Enemy enemy;
        // TODO remove after demo
        if (combatants.getFirst().getName().equals("demo")) {
            enemy = new Enemy(EnemyTemplate.values()[new Random().nextInt(EnemyTemplate.values().length - 1)]);
        } else {
            // TODO Make enemy selected based on number of user characters instead of randomly
            enemy = new Enemy(EnemyTemplate.values()[new Random().nextInt(EnemyTemplate.values().length - 1)]);
        }
        List<UserCharacterDto> characterDtos = currentCombat.getCombatants().stream().map(UserCharacter::toDto).toList();
        CombatResponse combatResponse = new CombatResponse(currentCombat.getCombatId(), enemy, characterDtos, currentCombat.getSeed());
        finishCombat();
        return new ResponseEntity<>(combatResponse, HttpStatus.OK);
    }

    private void finishCombat() {
        currentCombat.setCombatState(Combat.CombatState.CONCLUDED);
        currentCombat.setConclusionTs(Instant.now());
        // TODO send savedCombat to battle simulator
        // TODO convert simulator results to combatEvents
        combatRepository.save(currentCombat);
        currentCombat = null;
        // TODO generate and divide loot
    }

    public ResponseEntity<Combat> getLastCombat() {
        Combat dbCombat = combatRepository.findFirstOrderByCreatedTsDesc();
        return new ResponseEntity<>(dbCombat, HttpStatus.OK);
    }
}
