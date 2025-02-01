package com.kingargaroth.argoniarpg.services;

import com.kingargaroth.argoniarpg.entities.Equipment;
import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.repositories.CharacterRepository;
import com.kingargaroth.argoniarpg.repositories.EquipmentRepository;
import com.kingargaroth.argoniarpg.request.CharacterActionRequest;
import com.kingargaroth.argoniarpg.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class EquipmentService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    public ResponseEntity<UserCharacter> lootEquipment(CharacterActionRequest object, Long equipmentId) {
        Equipment equipment = equipmentRepository.findFirstByEquipmentId(equipmentId);
        if (equipment == null) {
            return ErrorCode.EQUIPMENT_NOT_FOUND.createErrorResponse(null, equipmentId);
        }
        UserCharacter userCharacter = characterRepository.findFirstByTwitchUserId(object.getTwitchUserId());
        if (userCharacter == null) {
            return ErrorCode.CHARACTER_NOT_FOUND.createErrorResponse(null, object.getTwitchUserId());
        }
        userCharacter.getEquipment().add(equipment);
        UserCharacter userCharacterDB = characterRepository.save(userCharacter);
        return new ResponseEntity<>(userCharacterDB, HttpStatus.OK);
    }
}
