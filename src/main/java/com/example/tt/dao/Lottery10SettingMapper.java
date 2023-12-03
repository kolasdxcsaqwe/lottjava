package com.example.tt.dao;

import com.example.tt.Bean.Lottery10Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery10SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery10Setting record);

    int insertSelective(Lottery10Setting record);

    Lottery10Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery10Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery10Setting record);

    int updateByPrimaryKey(Lottery10Setting record);

    Lottery10Setting selectByRoomId(@Param("roomid") Integer roomid);
}