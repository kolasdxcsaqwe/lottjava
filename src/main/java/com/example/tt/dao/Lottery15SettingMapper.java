package com.example.tt.dao;

import com.example.tt.Bean.Lottery15Setting;
import com.example.tt.Bean.Lottery1Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery15SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery15Setting record);

    int insertSelective(Lottery15Setting record);

    Lottery15Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery15Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery15Setting record);

    int updateByPrimaryKey(Lottery15Setting record);

    Lottery15Setting selectByRoomId(@Param("roomid") Integer roomId);
}