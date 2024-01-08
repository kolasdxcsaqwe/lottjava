package com.example.tt.dao;

import com.example.tt.Bean.QXCOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QXCOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QXCOrder record);

    int insertSelective(QXCOrder record);

    QXCOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QXCOrder record);

    int updateByPrimaryKeyWithBLOBs(QXCOrder record);

    int updateByPrimaryKey(QXCOrder record);

    List<QXCOrder> selectOrderByStatusAndTerm(@Param("status")Integer status,@Param("term")String term);

    List<QXCOrder> selectOrderByStatus(@Param("status")Integer status);

    int insertOrderList(List<QXCOrder> qxcOrders);
}