package com.hewutao.db.dao.base.dao.impl;

import com.hewutao.db.dao.base.dao.EndpointDAO;
import com.hewutao.db.dao.base.mapper.EndpointPOMapper;
import com.hewutao.db.dao.base.model.EndpointPO;
import com.hewutao.db.dao.base.model.EndpointPOExample;
import com.hewutao.db.dao.base.support.DbStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EndpointDAOImpl implements EndpointDAO {
    private EndpointPOMapper endpointPOMapper;

    @Override
    public List<EndpointPO> getAllUndeletedEndpointsByIds(List<String> endpointIds) {
        EndpointPOExample example = new EndpointPOExample();
        example.or()
                .andIdIn(endpointIds)
                .andStatusNotEqualTo(DbStatus.DELETED);


        return endpointPOMapper.selectByExample(example);
    }

    @Override
    public void add(EndpointPO endpointPO) {
        endpointPOMapper.insert(endpointPO);
    }

    @Override
    public void update(EndpointPO endpointPO) {
        endpointPOMapper.updateByPrimaryKey(endpointPO);
    }
}
