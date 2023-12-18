package com.example.tt.dao;

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.Bean.Lottery22Setting;

public interface Lottery22SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery22Setting record);

    int insertSelective(Lottery22Setting record);

    Lottery22Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery22Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery22Setting record);

    int updateByPrimaryKey(Lottery22Setting record);

    Lottery22Setting selectByRoomId(Integer roomid);

    int updateOrInsertById(Lottery22Setting record);
}