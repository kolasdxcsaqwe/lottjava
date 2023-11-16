package com.example.tt.Controller;

import com.example.tt.Bean.ChatBean;
import com.example.tt.dao.ChatBeanMapper;
import com.example.tt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    ChatBeanMapper chatBeanMapper;

    @ResponseBody
    @RequestMapping(value = "/NewChats",method = RequestMethod.POST)
    public JSONObject NewChats(@RequestParam(name ="userid")String userid,
                                   @RequestParam(name ="game")String game,
                                   @RequestParam(name ="roomid")String roomid)
    {
        if(Strings.isNullAmongOf(userid,game,roomid))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        List<ChatBean> list=chatBeanMapper.last50Row(roomid,game);
        int nowTerm=GameIndex.getLotteryIndex(game);
        if(nowTerm < 1)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1);
        }

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getUserid().equals(userid))
            {
                list.get(i).setType("U2");
            }
        }

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("betTerm",game);
            jsonObject.put("list",list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ReturnDataBuilder.makeBaseJSON(jsonObject);
    }

    @ResponseBody
    @RequestMapping(value = "/UpdateChats",method = RequestMethod.POST)
    public JSONObject UpdateChats(@RequestParam(name ="userid")String userid,
                                  @RequestParam(name ="chatid")String chatid,
                               @RequestParam(name ="game")String game,
                               @RequestParam(name ="roomid")String roomid)
    {
        if(Strings.isNullAmongOf(userid,game,roomid,chatid) && Strings.isDigitOnly(chatid))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        List<ChatBean> list=chatBeanMapper.selectNewChat(roomid,Integer.valueOf(chatid),game);
        List<ChatBean> newList=new ArrayList<>();
        if(list.size()>0)
        {
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getUserid().equals(userid))
                {
                    newList.add(list.get(i));
                }
            }
        }

        return ReturnDataBuilder.makeBaseJSON(newList);
    }


}
