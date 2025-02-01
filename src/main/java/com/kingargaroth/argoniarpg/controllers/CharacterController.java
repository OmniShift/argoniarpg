package com.kingargaroth.argoniarpg.controllers;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import com.kingargaroth.argoniarpg.request.CharacterActionRequest;
import com.kingargaroth.argoniarpg.request.CreateCharacterRequest;
import com.kingargaroth.argoniarpg.services.CharacterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/character")
@Slf4j
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @PostMapping
    public ResponseEntity<UserCharacter> createCharacter(@Valid @RequestBody CreateCharacterRequest object) {
        return characterService.createCharacter(object.getTwitchUserId(), object);
    }

    @Deprecated(forRemoval = true) // Will be removed before release
    @PutMapping("/spell/add/{spellValue}")
    public ResponseEntity<UserCharacter> addSpell(CharacterActionRequest object,
                                                  @PathVariable String spellValue) {
        return characterService.addSpell(object, spellValue);
    }
}
