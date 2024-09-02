package com.kingargaroth.argoniarpg.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingargaroth.argoniarpg.entities.Feat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
@Slf4j
public class FeatConverter implements AttributeConverter<List<Feat>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<Feat>> typeRef = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(List<Feat> feats) {

        if (feats == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(feats);
        } catch (JsonProcessingException e) {
            log.error("Couldn't convert feat list %s to JSON String".formatted(feats), e);
            return null;
        }
    }

    @Override
    public List<Feat> convertToEntityAttribute(String jsonString) {

        if (jsonString == null || jsonString.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            log.error("Couldn't convert JSON String %s to feat list".formatted(jsonString), e);
            return null;
        }
    }
}
