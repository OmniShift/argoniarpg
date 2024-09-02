package com.kingargaroth.argoniarpg.repositories;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepository extends CrudRepository<UserCharacter, Long> {
    UserCharacter findFirstByTwitchUsernameAndName(String twitchUser, String characterName);
    UserCharacter findFirstByTwitchUsernameAndCharacterId(String twitchUser, long id);
}
