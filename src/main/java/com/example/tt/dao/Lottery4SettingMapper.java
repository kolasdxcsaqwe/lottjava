package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery4Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery4SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery4Setting record);

    int insertSelective(Lottery4Setting record);

    Lottery4Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery4Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery4Setting record);

    int updateByPrimaryKey(Lottery4Setting record);

    Lottery4Setting selectByRoomId(@Param("roomid") Integer roomId);
}