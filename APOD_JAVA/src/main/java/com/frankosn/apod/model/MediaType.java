package com.frankosn.apod.model;

/**
 * Enum for APOD media types.
 */
public enum MediaType {
    IMAGE("image"),
    VIDEO("video"),
    UNKNOWN("unknown");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MediaType fromString(String value) {
        for (MediaType type : MediaType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
