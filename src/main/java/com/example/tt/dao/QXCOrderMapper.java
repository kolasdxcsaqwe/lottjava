package com.example.tt.dao;

import com.example.tt.Bean.QXCOrder;

public interface QXCOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QXCOrder record);

    int insertSelective(QXCOrder record);

    QXCOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QXCOrder record);

    int updateByPrimaryKeyWithBLOBs(QXCOrder record);

    int updateByPrimaryKey(QXCOrder record);
}