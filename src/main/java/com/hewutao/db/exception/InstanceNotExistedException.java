package com.hewutao.db.exception;

public class InstanceNotExistedException extends BusinessException {
    private final String instanceId;

    public InstanceNotExistedException(String instanceId) {
        super(ErrorCode.INSTANCE_NOT_EXISTED, "Instance [" + instanceId + "] not existed");
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
