package com.hewutao.db.model;

public enum InstanceMode {
    HA,
    SINGLE;

    public static InstanceMode from(String mode) {
        for (InstanceMode item : InstanceMode.values()) {
            if (item.name().equalsIgnoreCase(mode)) {
                return item;
            }
        }

        throw new IllegalArgumentException("[" + mode + "] is illegal mode");
    }
}
