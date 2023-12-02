package com.example.tt.dao;

import com.example.tt.Bean.TopUpLog;

public interface TopUpLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopUpLog record);

    int insertSelective(TopUpLog record);

    TopUpLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopUpLog record);

    int updateByPrimaryKeyWithBLOBs(TopUpLog record);

    int updateByPrimaryKey(TopUpLog record);
}