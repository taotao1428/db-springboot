package com.hewutao.db.dao.base.mapper;

import com.hewutao.db.dao.base.model.EndpointPO;
import com.hewutao.db.dao.base.model.EndpointPOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EndpointPOMapper {
    long countByExample(EndpointPOExample example);

    int deleteByExample(EndpointPOExample example);

    int deleteByPrimaryKey(String id);

    int insert(EndpointPO record);

    int insertSelective(EndpointPO record);

    List<EndpointPO> selectByExample(EndpointPOExample example);

    EndpointPO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") EndpointPO record, @Param("example") EndpointPOExample example);

    int updateByExample(@Param("record") EndpointPO record, @Param("example") EndpointPOExample example);

    int updateByPrimaryKeySelective(EndpointPO record);

    int updateByPrimaryKey(EndpointPO record);
}