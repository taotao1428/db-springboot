package com.hewutao.db.model;

public enum EntityStatus {
    CREATING,
    NORMAL,
    ABNORMAL,
    DELETED;

    public static EntityStatus from(String status) {
        for (EntityStatus item : values()) {
            if (item.name().equalsIgnoreCase(status)) {
                return item;
            }
        }

        throw new IllegalArgumentException("not found status [" + status + "]");
    }
}
