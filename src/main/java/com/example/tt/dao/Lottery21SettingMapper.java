package com.example.tt.dao;

import com.example.tt.Bean.Lottery21Setting;
import com.example.tt.Bean.Lottery22Setting;

public interface Lottery21SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery21Setting record);

    int insertSelective(Lottery21Setting record);

    Lottery21Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lottery21Setting record);

    int updateByPrimaryKeyWithBLOBs(Lottery21Setting record);

    int updateByPrimaryKey(Lottery21Setting record);

    Lottery21Setting selectByRoomId(Integer roomid);
}