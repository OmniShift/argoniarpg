package com.kingargaroth.argoniarpg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Job {
    GUARD("guard"),
    THIEF("thief"),
    WIZARD("wizard");

    private final String name;
}
