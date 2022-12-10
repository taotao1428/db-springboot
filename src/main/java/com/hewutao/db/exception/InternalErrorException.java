package com.hewutao.db.exception;

public class InternalErrorException extends BusinessException {
    public InternalErrorException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR, message, cause);
    }

    public InternalErrorException(Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR, "internal error", cause);
    }
}
