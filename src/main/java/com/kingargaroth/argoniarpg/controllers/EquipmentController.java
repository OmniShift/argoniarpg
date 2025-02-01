package com.kingargaroth.argoniarpg.controllers;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.request.CharacterActionRequest;
import com.kingargaroth.argoniarpg.services.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipment")
@Slf4j
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    @PutMapping("/loot/{equipmentId}")
    public ResponseEntity<UserCharacter> lootEquipment(CharacterActionRequest object,
                                                       @PathVariable Long equipmentId) {
        return equipmentService.lootEquipment(object, equipmentId);
    }
}
