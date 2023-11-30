package com.example.tt.dao;

import com.example.tt.Bean.BanWords;
import org.apache.ibatis.annotations.Param;

public interface BanWordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BanWords record);

    int insertSelective(BanWords record);

    BanWords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BanWords record);

    int updateByPrimaryKeyWithBLOBs(BanWords record);

    int updateByPrimaryKey(BanWords record);

    BanWords selectBanUser(@Param("roomid") int roomid, @Param("userid")String userid);
}