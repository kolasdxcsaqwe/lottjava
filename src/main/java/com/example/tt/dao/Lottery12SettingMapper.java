package com.example.tt.dao;

import com.example.tt.Bean.Lottery12Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery12SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery12Setting record);

    int insertSelective(Lottery12Setting record);

    Lottery12Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery12Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery12Setting record);

    int updateByPrimaryKey(Lottery12Setting record);

    Lottery12Setting selectByRoomId(@Param("roomid") Integer roomId);


}