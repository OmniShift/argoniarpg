package com.kingargaroth.argoniarpg.response;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
public enum ErrorCode {

    CHARACTER_NOT_FOUND(101, HttpStatus.CONFLICT, "Character for user %s not found"),
    ALL_SPELLS_KNOWN(102, HttpStatus.CONFLICT, "Character already knows all spells"),
    SPELL_KNOWN(103, HttpStatus.BAD_REQUEST, "Character already knows %s"),
    USER_HAS_CHARACTER(103, HttpStatus.CONFLICT, "Cannot create new character for user because they already have a character"),

    EQUIPMENT_NOT_FOUND(201, HttpStatus.BAD_REQUEST, "Equipment %d not found"),

    COMBAT_STARTED(301, HttpStatus.CONFLICT, "Combat has already started, but the results not yet retrieved"),
    NO_COMBAT(302, HttpStatus.CONFLICT, "No combat currently underway"),
    COMBAT_ALREADY_JOINED(303, HttpStatus.CONFLICT, "%s already joined the fight");

    final int code;
    final HttpStatus status;
    final String description;

    public <T> ResponseEntity<T> createErrorResponse(T body, Object... descriptionParams) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Error-Code", String.valueOf(this.code));
        headers.add("Error-Description", this.description.formatted(descriptionParams));
        return new ResponseEntity<>(body, headers, this.status);
    }
}
