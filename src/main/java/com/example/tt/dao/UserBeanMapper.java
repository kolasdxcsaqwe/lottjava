package com.example.tt.dao;

import com.example.tt.Bean.UserBean;

public interface UserBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBean record);

    int insertSelective(UserBean record);

    UserBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBean record);

    int updateByPrimaryKeyWithBLOBs(UserBean record);

    int updateByPrimaryKey(UserBean record);
}