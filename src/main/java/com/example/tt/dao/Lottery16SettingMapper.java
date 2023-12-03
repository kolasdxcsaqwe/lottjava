package com.example.tt.dao;

import com.example.tt.Bean.Lottery16Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery16SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery16Setting record);

    int insertSelective(Lottery16Setting record);

    Lottery16Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery16Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery16Setting record);

    int updateByPrimaryKey(Lottery16Setting record);

    Lottery16Setting selectByRoomId(@Param("roomid") Integer roomId);
}