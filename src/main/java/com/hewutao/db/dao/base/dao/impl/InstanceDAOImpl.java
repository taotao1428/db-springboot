package com.hewutao.db.dao.base.dao.impl;

import com.hewutao.db.dao.base.dao.InstanceDAO;
import com.hewutao.db.dao.base.mapper.InstancePOMapper;
import com.hewutao.db.dao.base.model.InstancePO;
import com.hewutao.db.dao.base.model.InstancePOExample;
import com.hewutao.db.dao.base.support.DbStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class InstanceDAOImpl implements InstanceDAO {

    private InstancePOMapper instancePOMapper;

    @Override
    public InstancePO getUndeleteById(String id) {
        InstancePOExample example = new InstancePOExample();
        example.or()
                .andIdEqualTo(id)
                .andStatusNotEqualTo(DbStatus.DELETED);

        List<InstancePO> instancePOS = instancePOMapper.selectByExample(example);

        return instancePOS.isEmpty() ? null : instancePOS.get(0);
    }

    @Override
    public void add(InstancePO instancePO) {
        instancePOMapper.insert(instancePO);
    }

    @Override
    public void update(InstancePO instancePO) {
        instancePOMapper.updateByPrimaryKey(instancePO);
    }

    @Override
    public List<InstancePO> queryUndeletedInstances(String id, String name, String mode, String engineId) {
        InstancePOExample example = new InstancePOExample();
        InstancePOExample.Criteria criteria = example.or();

        if (id != null) {
            criteria.andIdEqualTo(id);
        }
        if (name != null) {
            criteria.andNameEqualTo(name);
        }
        if (mode != null) {
            criteria.andModeEqualTo(mode);
        }
        if (engineId != null) {
            criteria.andEngineIdEqualTo(engineId);
        }

        // 不要查询出已经删除的实例
        criteria.andStatusNotEqualTo(DbStatus.DELETED);

        return instancePOMapper.selectByExample(example);
    }
}
