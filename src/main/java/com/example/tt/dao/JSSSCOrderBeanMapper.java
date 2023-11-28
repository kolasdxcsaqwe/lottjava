package com.example.tt.dao;

import com.example.tt.Bean.JSSSCOrderBean;

public interface JSSSCOrderBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JSSSCOrderBean record);

    int insertSelective(JSSSCOrderBean record);

    JSSSCOrderBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JSSSCOrderBean record);

    int updateByPrimaryKeyWithBLOBs(JSSSCOrderBean record);

    int updateByPrimaryKey(JSSSCOrderBean record);
}