package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery1SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery1Setting record);

    int insertSelective(Lottery1Setting record);

    Lottery1Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery1Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery1Setting record);

    int updateByPrimaryKey(Lottery1Setting record);

    Lottery1Setting selectByRoomId(@Param("roomid") Integer roomId);
}