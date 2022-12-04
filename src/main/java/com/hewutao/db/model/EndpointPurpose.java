package com.hewutao.db.model;

public enum EndpointPurpose {
    DATA,
    MANAGEMENT;

    public static EndpointPurpose from(String purpose) {
        for (EndpointPurpose item : EndpointPurpose.values()) {
            if (item.name().equalsIgnoreCase(purpose)) {
                return item;
            }
        }
        throw new IllegalArgumentException("invalid purpose [" + purpose + "]");
    }
}
