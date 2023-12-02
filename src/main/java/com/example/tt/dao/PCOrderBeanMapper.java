package com.example.tt.dao;

import com.example.tt.Bean.PCOrderBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PCOrderBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PCOrderBean record);

    int insertSelective(PCOrderBean record);

    PCOrderBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PCOrderBean record);

    int updateByPrimaryKeyWithBLOBs(PCOrderBean record);

    int updateByPrimaryKey(PCOrderBean record);

    List<PCOrderBean> selectByStatus(String status);

    Integer sumMoneyByTerm(@Param("roomid")String roomid,@Param("term")String term,@Param("userid")String userid);
}