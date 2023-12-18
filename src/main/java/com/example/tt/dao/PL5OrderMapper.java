package com.example.tt.dao;

import com.example.tt.Bean.PL5Order;

public interface PL5OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PL5Order record);

    int insertSelective(PL5Order record);

    PL5Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PL5Order record);

    int updateByPrimaryKeyWithBLOBs(PL5Order record);

    int updateByPrimaryKey(PL5Order record);
}