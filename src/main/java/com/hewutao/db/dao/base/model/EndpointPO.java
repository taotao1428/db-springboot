package com.hewutao.db.dao.base.model;

public class EndpointPO {
    private String id;

    private String ip;

    private String iaasId;

    private String purpose;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getIaasId() {
        return iaasId;
    }

    public void setIaasId(String iaasId) {
        this.iaasId = iaasId == null ? null : iaasId.trim();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose == null ? null : purpose.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}