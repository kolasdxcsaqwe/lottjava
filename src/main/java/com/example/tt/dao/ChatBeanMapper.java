package com.example.tt.dao;

import com.example.tt.Bean.ChatBean;

import java.util.List;

public interface ChatBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChatBean record);

    int insertSelective(ChatBean record);

    ChatBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChatBean record);

    int updateByPrimaryKeyWithBLOBs(ChatBean record);

    int updateByPrimaryKey(ChatBean record);

    List<ChatBean> last50Row(String roomid,String game);

    List<ChatBean> selectNewChat(String roomid,Integer chatid,String game);
}