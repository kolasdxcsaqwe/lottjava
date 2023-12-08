package com.example.tt.dao;

import com.example.tt.Bean.UserBean;
import org.apache.ibatis.annotations.Param;

public interface UserBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBean record);

    int insertSelective(UserBean record);

    UserBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBean record);

    int updateByPrimaryKeyWithBLOBs(UserBean record);

    int updateByPrimaryKey(UserBean record);

    UserBean selectByUserId(@Param("userid") String userId);

    UserBean selectByUserAndRoomId(@Param("userName") String userName,@Param("roomId") Integer roomId,@Param("agent") String agent);

    UserBean selectByUserAndRoomIdAndPassword(@Param("userName") String userName,@Param("roomId") Integer roomId,@Param("password") String password,@Param("agent") String agent);


}