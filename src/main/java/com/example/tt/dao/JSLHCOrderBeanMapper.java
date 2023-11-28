package com.example.tt.dao;

import com.example.tt.Bean.JSLHCOrderBean;

public interface JSLHCOrderBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JSLHCOrderBean record);

    int insertSelective(JSLHCOrderBean record);

    JSLHCOrderBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JSLHCOrderBean record);

    int updateByPrimaryKeyWithBLOBs(JSLHCOrderBean record);

    int updateByPrimaryKey(JSLHCOrderBean record);
}