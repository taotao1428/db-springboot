package com.hewutao.db.dao.base.dao;

import com.hewutao.db.dao.base.model.InstancePO;

import java.util.List;

public interface InstanceDAO {
    InstancePO getUndeleteById(String id);

    void add(InstancePO instancePO);

    void update(InstancePO instancePO);

    List<InstancePO> queryUndeletedInstances(String id, String name, String mode, String engineId);
}
