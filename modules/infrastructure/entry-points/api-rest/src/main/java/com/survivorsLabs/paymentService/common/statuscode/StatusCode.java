package com.survivorsLabs.muscleCoach.common.statuscode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusCode {

    private Level level;
    private String code;
    private String message;
    private String extCode;

    public enum Level {
        SUCCESS("00"),
        WARN("98"),
        FAIL("99");

        private final String codeDefault;

        Level(String codeI) {
            this.codeDefault = codeI;
        }

        public String value() {
            return codeDefault;
        }

    }
}