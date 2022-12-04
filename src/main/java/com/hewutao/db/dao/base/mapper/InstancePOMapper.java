package com.hewutao.db.dao.base.mapper;

import com.hewutao.db.dao.base.model.InstancePO;
import com.hewutao.db.dao.base.model.InstancePOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InstancePOMapper {
    long countByExample(InstancePOExample example);

    int deleteByExample(InstancePOExample example);

    int deleteByPrimaryKey(String id);

    int insert(InstancePO record);

    int insertSelective(InstancePO record);

    List<InstancePO> selectByExample(InstancePOExample example);

    InstancePO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") InstancePO record, @Param("example") InstancePOExample example);

    int updateByExample(@Param("record") InstancePO record, @Param("example") InstancePOExample example);

    int updateByPrimaryKeySelective(InstancePO record);

    int updateByPrimaryKey(InstancePO record);
}