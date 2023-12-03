package com.example.tt.dao;

import com.example.tt.Bean.Lottery1Setting;
import com.example.tt.Bean.Lottery5Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery5SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery5Setting record);

    int insertSelective(Lottery5Setting record);

    Lottery5Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery5Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery5Setting record);

    int updateByPrimaryKey(Lottery5Setting record);

    Lottery5Setting selectByRoomId(@Param("roomid") Integer roomId);
}