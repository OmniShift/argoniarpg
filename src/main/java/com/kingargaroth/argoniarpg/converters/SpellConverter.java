package com.kingargaroth.argoniarpg.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingargaroth.argoniarpg.entities.Spell;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
@Slf4j
public class SpellConverter implements AttributeConverter<List<Spell>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<Spell>> typeRef = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(List<Spell> spells) {

        if (spells == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(spells);
        } catch (JsonProcessingException e) {
            log.error("Couldn't convert spell list to JSON String.", e);
            return null;
        }
    }

    @Override
    public List<Spell> convertToEntityAttribute(String jsonString) {

        if (jsonString == null || jsonString.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            log.error("Couldn't convert JSON String to spell list", e);
            return null;
        }
    }
}
