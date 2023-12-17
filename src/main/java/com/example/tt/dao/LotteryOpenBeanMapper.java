package com.example.tt.dao;

import com.example.tt.Bean.LotteryOpenBean;
import org.apache.ibatis.annotations.Param;

public interface LotteryOpenBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LotteryOpenBean record);

    int insertSelective(LotteryOpenBean record);

    LotteryOpenBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LotteryOpenBean record);

    int updateByPrimaryKeyWithBLOBs(LotteryOpenBean record);

    int updateByPrimaryKey(LotteryOpenBean record);

    String getOpenByTerm(Integer type);

    LotteryOpenBean getLastOpenData(Integer type);

    String getOpenCodesByTermAndType(@Param("type") Integer type, @Param("term")String term);
}