package com.kingargaroth.argoniarpg.entities.helpers;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class EnemyConverter implements AttributeConverter<Enemy, String> {
    @Override
    public String convertToDatabaseColumn(Enemy enemy) {
        if (enemy == null) {
            return null;
        }
        return "name=%s,level=%d,hp=%d,maxHp=%d,attack=%d,defense=%d".formatted(enemy.getName(), enemy.getLevel(),
                enemy.getHp(), enemy.getMaxHp(), enemy.getAttack(), enemy.getDefense());
    }

    @Override
    public Enemy convertToEntityAttribute(String dbString) {
        if (dbString == null || dbString.isEmpty()) {
            return null;
        }

        String[] parts = dbString.split(",");
        int expectedAttributes = 6;
        if (parts.length != expectedAttributes) {
            log.warn("Parsed enemy [ %s ] does not have expected %d attributes. Parsing may be incomplete"
                    .formatted(dbString, expectedAttributes));
        }

        Enemy enemy = new Enemy();
        for (String part : parts) {
            String value = dbString.split("=")[1];
            switch (part) {
                case "name" -> enemy.setName(value);
                case "level" -> enemy.setLevel(Integer.parseInt(value));
                case "hp" -> enemy.setHp(Integer.parseInt(value));
                case "maxHp" -> enemy.setMaxHp(Integer.parseInt(value));
                case "attack" -> enemy.setAttack(Integer.parseInt(value));
                case "defense" -> enemy.setDefense(Integer.parseInt(value));
            }
        }

        return enemy;
    }
}
