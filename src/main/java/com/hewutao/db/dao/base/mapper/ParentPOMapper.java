package com.hewutao.db.dao.base.mapper;

import com.hewutao.db.dao.base.model.ParentPO;
import com.hewutao.db.dao.base.model.ParentPOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParentPOMapper {
    long countByExample(ParentPOExample example);

    int deleteByExample(ParentPOExample example);

    int deleteByPrimaryKey(String id);

    int insert(ParentPO record);

    int insertSelective(ParentPO record);

    List<ParentPO> selectByExample(ParentPOExample example);

    ParentPO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ParentPO record, @Param("example") ParentPOExample example);

    int updateByExample(@Param("record") ParentPO record, @Param("example") ParentPOExample example);

    int updateByPrimaryKeySelective(ParentPO record);

    int updateByPrimaryKey(ParentPO record);
}