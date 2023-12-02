package com.example.tt.dao;

import com.example.tt.Bean.LotteryRoomSetting;

public interface LotteryRoomSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LotteryRoomSetting record);

    int insertSelective(LotteryRoomSetting record);

    LotteryRoomSetting selectByPrimaryKey(Integer id);

    LotteryRoomSetting selectByRoomId(Integer id);

    int updateByPrimaryKeySelective(LotteryRoomSetting record);

    int updateByPrimaryKeyWithBLOBs(LotteryRoomSetting record);

    int updateByPrimaryKey(LotteryRoomSetting record);
}