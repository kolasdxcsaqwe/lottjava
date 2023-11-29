package com.example.tt.dao;

import com.example.tt.Bean.ChatBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChatBean record);

    int insertSelective(ChatBean record);

    ChatBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChatBean record);

    int updateByPrimaryKeyWithBLOBs(ChatBean record);

    int updateByPrimaryKey(ChatBean record);

    List<ChatBean> last50RowByGame(@Param("roomid") int roomid, @Param("game")String game);

    List<ChatBean> last50RowByRoom(@Param("roomid") int roomid);


    List<ChatBean> selectNewChat(String roomid,Integer chatid,String game);
}