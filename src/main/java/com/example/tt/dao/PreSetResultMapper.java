package com.example.tt.dao;

import com.example.tt.Bean.PreSetResult;
import org.apache.ibatis.annotations.Param;

public interface PreSetResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSetResult record);

    int insertSelective(PreSetResult record);

    PreSetResult selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSetResult record);

    int updateByPrimaryKey(PreSetResult record);

    PreSetResult selectByTermAndType(@Param("type")String type, @Param("term")String term);
}