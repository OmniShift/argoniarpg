package com.kingargaroth.argoniarpg.repositories;

import com.kingargaroth.argoniarpg.entities.Combat;
import org.springframework.data.repository.CrudRepository;

public interface CombatRepository extends CrudRepository<Combat, Long> {
    Combat findFirstOrderByCreatedTsDesc();
}
