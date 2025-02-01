package com.kingargaroth.argoniarpg.response;

import com.kingargaroth.argoniarpg.entities.helpers.Enemy;

import java.util.List;

public record CombatResponse(long combatId, Enemy enemy, List<UserCharacterDto> combatants, String seed) {
    // empty record
}
