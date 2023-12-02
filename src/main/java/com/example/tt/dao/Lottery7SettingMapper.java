package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery7Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery7SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery7Setting record);

    int insertSelective(Lottery7Setting record);

    Lottery7Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery7Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery7Setting record);

    int updateByPrimaryKey(Lottery7Setting record);

    Lottery7Setting selectByRoomId(@Param("roomid") Integer roomId);
}