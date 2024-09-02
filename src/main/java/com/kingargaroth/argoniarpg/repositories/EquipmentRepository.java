package com.kingargaroth.argoniarpg.repositories;

import com.kingargaroth.argoniarpg.entities.Equipment;
import org.springframework.data.repository.CrudRepository;

public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    Equipment findFirstByEquipmentId(Long equipmentId);
}
