package com.example.tt.dao;

import com.example.tt.Bean.Lottery11Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery11SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery11Setting record);

    int insertSelective(Lottery11Setting record);

    Lottery11Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery11Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery11Setting record);

    int updateByPrimaryKey(Lottery11Setting record);

    Lottery11Setting selectByRoomId(@Param("roomid") Integer roomid);
}