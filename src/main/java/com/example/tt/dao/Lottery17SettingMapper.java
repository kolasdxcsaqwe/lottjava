package com.example.tt.dao;

import com.example.tt.Bean.Lottery17Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery17SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery17Setting record);

    int insertSelective(Lottery17Setting record);

    Lottery17Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery17Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery17Setting record);

    int updateByPrimaryKey(Lottery17Setting record);

    Lottery17Setting selectByRoomId(@Param("roomid") Integer roomId);
}