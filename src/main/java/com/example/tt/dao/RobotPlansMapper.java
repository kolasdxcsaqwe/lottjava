package com.example.tt.dao;

import com.example.tt.Bean.RobotPlans;

import java.util.List;

public interface RobotPlansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotPlans record);

    int insertSelective(RobotPlans record);

    RobotPlans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotPlans record);

    int updateByPrimaryKeyWithBLOBs(RobotPlans record);

    int updateByPrimaryKey(RobotPlans record);

    List<RobotPlans> selectAll();
}