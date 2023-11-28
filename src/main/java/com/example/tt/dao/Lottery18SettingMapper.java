package com.example.tt.dao;

import com.example.tt.Bean.Lottery18Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery18SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery18Setting record);

    int insertSelective(Lottery18Setting record);

    Lottery18Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery18Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery18Setting record);

    int updateByPrimaryKey(Lottery18Setting record);

    Lottery18Setting selectByRoomId(@Param("roomid") Integer roomId);
}