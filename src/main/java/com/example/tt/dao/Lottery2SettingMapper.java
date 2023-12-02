package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery2Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery2SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery2Setting record);

    int insertSelective(Lottery2Setting record);

    Lottery2Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery2Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery2Setting record);

    int updateByPrimaryKey(Lottery2Setting record);

    Lottery2Setting selectByRoomId(@Param("roomid") Integer roomId);
}