package com.example.tt.dao;

import com.example.tt.Bean.MarkLog;

public interface MarkLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarkLog record);

    int insertSelective(MarkLog record);

    MarkLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarkLog record);

    int updateByPrimaryKeyWithBLOBs(MarkLog record);

    int updateByPrimaryKey(MarkLog record);
}