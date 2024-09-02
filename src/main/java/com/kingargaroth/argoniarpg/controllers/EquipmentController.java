package com.kingargaroth.argoniarpg.controllers;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.entities.Equipment;
import com.kingargaroth.argoniarpg.repositories.CharacterRepository;
import com.kingargaroth.argoniarpg.repositories.EquipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipment")
@Slf4j
public class EquipmentController {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @PutMapping("/loot/{equipmentId}")
    public ResponseEntity<UserCharacter> lootEquipment(@RequestHeader("Twitch-Username") String twitchUser,
                                                       @RequestHeader("Character-Name") String characterName,
                                                       @PathVariable Long equipmentId) throws Exception {
        Equipment equipment = equipmentRepository.findFirstByEquipmentId(equipmentId);
        if (equipment == null) {
            throw new Exception("Equipment %d not found".formatted(equipmentId));
        }
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUsernameAndName(twitchUser, characterName);
        if (userCharacter == null) {
            throw new Exception("Character for user %s not found".formatted(twitchUser));
        }
        userCharacter.getEquipment().add(equipment);
        UserCharacter userCharacterDB = characterRepository.save(userCharacter);
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }
}
