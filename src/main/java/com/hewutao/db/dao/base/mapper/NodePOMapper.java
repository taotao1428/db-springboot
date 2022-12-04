package com.hewutao.db.dao.base.mapper;

import com.hewutao.db.dao.base.model.NodePO;
import com.hewutao.db.dao.base.model.NodePOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NodePOMapper {
    long countByExample(NodePOExample example);

    int deleteByExample(NodePOExample example);

    int deleteByPrimaryKey(String id);

    int insert(NodePO record);

    int insertSelective(NodePO record);

    List<NodePO> selectByExample(NodePOExample example);

    NodePO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") NodePO record, @Param("example") NodePOExample example);

    int updateByExample(@Param("record") NodePO record, @Param("example") NodePOExample example);

    int updateByPrimaryKeySelective(NodePO record);

    int updateByPrimaryKey(NodePO record);
}