package com.hewutao.db.exception;

public enum ErrorCode {
    INSTANCE_NOT_EXISTED(100001),
    PARAM_INVALID(100002),
    INTERNAL_ERROR(100003);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }
}
