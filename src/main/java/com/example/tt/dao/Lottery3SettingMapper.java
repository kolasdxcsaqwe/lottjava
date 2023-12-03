package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery3Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery3SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery3Setting record);

    int insertSelective(Lottery3Setting record);

    Lottery3Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery3Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery3Setting record);

    int updateByPrimaryKey(Lottery3Setting record);

    Lottery3Setting selectByRoomId(@Param("roomid") Integer roomId);
}