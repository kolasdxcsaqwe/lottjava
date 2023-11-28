package com.example.tt.dao;

import com.example.tt.Bean.Lottery13Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery13SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery13Setting record);

    int insertSelective(Lottery13Setting record);

    Lottery13Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery13Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery13Setting record);

    int updateByPrimaryKey(Lottery13Setting record);

    Lottery13Setting selectByRoomId(@Param("roomid") Integer roomId);
}