package com.kingargaroth.argoniarpg.controllers;

import com.kingargaroth.argoniarpg.entities.Combat;
import com.kingargaroth.argoniarpg.request.JoinCombatRequest;
import com.kingargaroth.argoniarpg.response.CombatResponse;
import com.kingargaroth.argoniarpg.services.CombatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/combat")
@Slf4j
public class CombatController {

    @Autowired
    private CombatService combatService;

    @PostMapping("/join")
    public ResponseEntity<String> joinCombat(JoinCombatRequest object) {
        return combatService.joinCombat(object);
    }

    @PostMapping("/start")
    public ResponseEntity<CombatResponse> startCombat() {
        return combatService.startCombat();
    }

    @GetMapping("/last")
    public ResponseEntity<Combat> getLastCombat() {
        return combatService.getLastCombat();
    }
}
