package com.example.tt.dao;

import com.example.tt.Bean.FC3DOrder;

public interface FC3DOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FC3DOrder record);

    int insertSelective(FC3DOrder record);

    FC3DOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FC3DOrder record);

    int updateByPrimaryKeyWithBLOBs(FC3DOrder record);

    int updateByPrimaryKey(FC3DOrder record);
}