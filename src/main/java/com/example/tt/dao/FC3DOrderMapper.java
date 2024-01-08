package com.example.tt.dao;

import com.example.tt.Bean.FC3DOrder;
import com.example.tt.Bean.PL5Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FC3DOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FC3DOrder record);

    int insertSelective(FC3DOrder record);

    FC3DOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FC3DOrder record);

    int updateByPrimaryKeyWithBLOBs(FC3DOrder record);

    int updateByPrimaryKey(FC3DOrder record);

    List<FC3DOrder> selectOrderByStatus(@Param("status")Integer status);

    List<FC3DOrder> selectOrderByStatusAndTerm(@Param("status")Integer status,@Param("term")String term);

    int insertOrderList(List<FC3DOrder> fc3DOrderList);
}