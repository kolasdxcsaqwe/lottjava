package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery8Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery8SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery8Setting record);

    int insertSelective(Lottery8Setting record);

    Lottery8Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery8Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery8Setting record);

    int updateByPrimaryKey(Lottery8Setting record);

    Lottery8Setting selectByRoomId(@Param("roomid") Integer roomId);
}