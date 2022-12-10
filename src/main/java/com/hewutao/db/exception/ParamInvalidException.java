package com.hewutao.db.exception;

public class ParamInvalidException extends BusinessException {

    public ParamInvalidException(String message) {
        super(ErrorCode.PARAM_INVALID, message);
    }

}
