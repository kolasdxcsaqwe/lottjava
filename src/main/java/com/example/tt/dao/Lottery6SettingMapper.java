package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery6Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery6SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery6Setting record);

    int insertSelective(Lottery6Setting record);

    Lottery6Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery6Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery6Setting record);

    int updateByPrimaryKey(Lottery6Setting record);

    Lottery6Setting selectByRoomId(@Param("roomid") Integer roomId);
}