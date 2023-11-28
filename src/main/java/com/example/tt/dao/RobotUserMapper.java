package com.example.tt.dao;

import com.example.tt.Bean.RobotUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RobotUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotUser record);

    int insertSelective(RobotUser record);

    RobotUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotUser record);

    int updateByPrimaryKeyWithBLOBs(RobotUser record);

    int updateByPrimaryKey(RobotUser record);

    List<RobotUser> selectRobotsByGame(@Param("roomid") String roomid,@Param("game") String game);
}