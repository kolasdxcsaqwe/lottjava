package com.example.tt.dao;

import com.example.tt.Bean.TWK3OrderBean;

public interface TWK3OrderBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TWK3OrderBean record);

    int insertSelective(TWK3OrderBean record);

    TWK3OrderBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TWK3OrderBean record);

    int updateByPrimaryKeyWithBLOBs(TWK3OrderBean record);

    int updateByPrimaryKey(TWK3OrderBean record);
}