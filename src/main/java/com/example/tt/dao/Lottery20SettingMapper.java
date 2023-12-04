package com.example.tt.dao;

import com.example.tt.Bean.Lottery19Setting;
import com.example.tt.Bean.Lottery20Setting;
import org.apache.ibatis.annotations.Param;

public interface Lottery20SettingMapper {
    int insert(Lottery20Setting record);

    int insertSelective(Lottery20Setting record);

    Lottery20Setting selectByRoomId(@Param("roomid") Integer roomId);

    int updateOrInsertById(Lottery20Setting lottery20Setting);
}