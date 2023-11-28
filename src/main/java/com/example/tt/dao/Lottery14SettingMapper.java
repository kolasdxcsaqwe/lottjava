package com.example.tt.dao;

import com.example.tt.Bean.Lottery14Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery14SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery14Setting record);

    int insertSelective(Lottery14Setting record);

    Lottery14Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery14Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery14Setting record);

    int updateByPrimaryKey(Lottery14Setting record);

    Lottery14Setting selectByRoomId(@Param("roomid") Integer roomId);
}