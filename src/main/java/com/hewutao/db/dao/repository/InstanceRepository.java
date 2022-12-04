package com.hewutao.db.dao.repository;

import com.hewutao.db.model.Instance;
import com.hewutao.db.model.QueryInstanceCondition;

import java.util.List;

public interface InstanceRepository {
    Instance getById(String id);
    void saveInstance(Instance instance);
    List<Instance> query(QueryInstanceCondition condition);
}
