package com.kingargaroth.argoniarpg.repositories;

import com.kingargaroth.argoniarpg.entities.UserCharacter;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepository extends CrudRepository<UserCharacter, Long> {
    UserCharacter findFirstByTwitchUserId(String twitchUser);
}
