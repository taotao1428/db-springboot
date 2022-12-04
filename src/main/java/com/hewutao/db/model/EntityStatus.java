package com.hewutao.db.model;

import com.hewutao.db.dao.base.support.DbStatus;

public enum EntityStatus {
    CREATING(DbStatus.CREATING),
    NORMAL(DbStatus.NORMAL),
    ABNORMAL(DbStatus.ABNORMAL),
    DELETED(DbStatus.DELETED);

    private final String dbStatus;

    EntityStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public static EntityStatus from(String dbStatus) {
        for (EntityStatus item : values()) {
            if (item.getDbStatus().equalsIgnoreCase(dbStatus)) {
                return item;
            }
        }

        throw new IllegalArgumentException("not found dbStatus [" + dbStatus + "]");
    }
}
