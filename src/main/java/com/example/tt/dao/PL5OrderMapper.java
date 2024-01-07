package com.example.tt.dao;

import com.example.tt.Bean.PL5Order;
import com.example.tt.Bean.QXCOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PL5OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PL5Order record);

    int insertSelective(PL5Order record);

    PL5Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PL5Order record);

    int updateByPrimaryKeyWithBLOBs(PL5Order record);

    int updateByPrimaryKey(PL5Order record);

    List<PL5Order> selectOrderByStatus(@Param("status")Integer status);

    List<PL5Order> selectOrderByStatusAndTerm(@Param("status")Integer status,@Param("term")String term);

    int insertOrderList(List<PL5Order> list);
}