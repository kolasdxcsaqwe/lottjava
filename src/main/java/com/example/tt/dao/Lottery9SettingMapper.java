package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery9Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery9SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery9Setting record);

    int insertSelective(Lottery9Setting record);

    Lottery9Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery9Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery9Setting record);

    int updateByPrimaryKey(Lottery9Setting record);

    Lottery9Setting selectByRoomId(@Param("roomid") Integer roomId);
}