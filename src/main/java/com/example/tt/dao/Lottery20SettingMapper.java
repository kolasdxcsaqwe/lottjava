package com.example.tt.dao;

import com.example.tt.Bean.Lottery20Setting;

public interface Lottery20SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery20Setting record);

    int insertSelective(Lottery20Setting record);

    Lottery20Setting selectByPrimaryKey(Integer id);

    Lottery20Setting selectByRoomId(Integer roomid);

    int updateByPrimaryKeySelective(Lottery20Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery20Setting record);

    int updateByPrimaryKey(Lottery20Setting record);

    int updateOrInsertById(Lottery20Setting record);
}