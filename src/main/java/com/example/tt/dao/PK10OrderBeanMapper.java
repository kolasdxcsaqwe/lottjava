package com.example.tt.dao;

import com.example.tt.Bean.PK10OrderBean;

public interface PK10OrderBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PK10OrderBean record);

    int insertSelective(PK10OrderBean record);

    PK10OrderBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PK10OrderBean record);

    int updateByPrimaryKeyWithBLOBs(PK10OrderBean record);

    int updateByPrimaryKey(PK10OrderBean record);
}