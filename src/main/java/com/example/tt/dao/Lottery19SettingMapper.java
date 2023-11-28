package com.example.tt.dao;

import com.example.tt.Bean.Lottery19Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery19SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery19Setting record);

    int insertSelective(Lottery19Setting record);

    Lottery19Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery19Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery19Setting record);

    int updateByPrimaryKey(Lottery19Setting record);

    Lottery19Setting selectByRoomId(@Param("roomid") Integer roomId);
}