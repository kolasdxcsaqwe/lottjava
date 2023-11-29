package com.example.tt.Controller;

import com.example.tt.Bean.ChatBean;
import com.example.tt.Bean.UserBean;
import com.example.tt.dao.*;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired(required = false)
    ChatBeanMapper chatBeanMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    @Autowired(required = false)
    Lottery1SettingMapper lottery1SettingMapper;

    @Autowired(required = false)
    Lottery2SettingMapper lottery2SettingMapper;

    @Autowired(required = false)
    Lottery3SettingMapper lottery3SettingMapper;

    @Autowired(required = false)
    Lottery4SettingMapper lottery4SettingMapper;

    @Autowired(required = false)
    Lottery5SettingMapper lottery5SettingMapper;

    @Autowired(required = false)
    Lottery6SettingMapper lottery6SettingMapper;

    @Autowired(required = false)
    Lottery7SettingMapper lottery7SettingMapper;

    @Autowired(required = false)
    Lottery8SettingMapper lottery8SettingMapper;

    @Autowired(required = false)
    Lottery9SettingMapper lottery9SettingMapper;

    @Autowired(required = false)
    Lottery10SettingMapper lottery10SettingMapper;

    @Autowired(required = false)
    Lottery11SettingMapper lottery11SettingMapper;

    @Autowired(required = false)
    Lottery12SettingMapper lottery12SettingMapper;

    @Autowired(required = false)
    Lottery13SettingMapper lottery13SettingMapper;

    @Autowired(required = false)
    Lottery14SettingMapper lottery14SettingMapper;

    @Autowired(required = false)
    Lottery15SettingMapper lottery15SettingMapper;

    @Autowired(required = false)
    Lottery16SettingMapper lottery16SettingMapper;

    @Autowired(required = false)
    Lottery17SettingMapper lottery17SettingMapper;

    @Autowired(required = false)
    Lottery18SettingMapper lottery18SettingMapper;

    @Autowired(required = false)
    Lottery19SettingMapper lottery19SettingMapper;

    static HashMap<String, UserBean> userBeanHashMap = new HashMap<>();

    @ResponseBody
    @RequestMapping(value = "/NewChats", method = RequestMethod.POST)
    public Object NewChats(@RequestParam(name = "userid") String userid,
                           @RequestParam(name = "game") String game,
                           @RequestParam(name = "roomid") String roomid) {
        if (Strings.isNullAmongOf(userid, game, roomid) || !Strings.isDigitOnly(roomid)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2).toString();
        }

        int roomId=Integer.valueOf(roomid);
        List<ChatBean> list = chatBeanMapper.last50RowByGame(Integer.valueOf(roomid), game);
        int nowTerm = GameIndex.getLotteryIndex(game);
        if (nowTerm < 1) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1).toString();
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserid().equals(userid)) {
                list.get(i).setType("U2");
            }
        }

        String betTerm=lotteryOpenBeanMapper.getOpenByTerm(nowTerm);
        if(betTerm==null)
        {
            betTerm="";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("betTerm", betTerm);
        map.put("list", list);

        return ReturnDataBuilder.makeBaseJSON(map);
    }

    @ResponseBody
    @RequestMapping(value = "/UpdateChats", method = RequestMethod.POST)
    public Object UpdateChats(@RequestParam(name = "userid") String userid,
                              @RequestParam(name = "chatid") String chatid,
                              @RequestParam(name = "game") String game,
                              @RequestParam(name = "roomid") String roomid) {
        if (Strings.isNullAmongOf(userid, game, roomid, chatid) || !Strings.isDigitOnly(chatid)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        List<ChatBean> list = chatBeanMapper.selectNewChat(roomid, Integer.valueOf(chatid), game);
        List<ChatBean> newList = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getUserid().equals(userid)) {
                    newList.add(list.get(i));
                }
            }
        }

        return ReturnDataBuilder.makeBaseJSON(newList);
    }

    @ResponseBody
    @RequestMapping(value = "/SendBet", method = RequestMethod.POST)
    public Object SendBet(@RequestParam(name = "userid") String userid,
                          @RequestParam(name = "content") String content,
                          @RequestParam(name = "game") String game,
                          @RequestParam(name = "roomid") String roomid) {
        if (Strings.isNullAmongOf(userid, game, roomid, content)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        UserBean userBean = userBeanHashMap.get(userid);
        if (userBean == null) {
            userBean = userBeanMapper.selectByUserId(userid);

            if (userBean == null) {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S3);
            } else {
                userBeanHashMap.put(userid, userBean);
            }
        }

        JSONObject gameSettingJson =getGameSetting(game,roomid);
        boolean isOpen=false;
        if (gameSettingJson == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }
        else
        {
            if(!gameSettingJson.optString("gameopen","").equals("false"))
            {
                isOpen=true;
            }
        }

        return ReturnDataBuilder.makeBaseJSON(null);
    }



    private JSONObject getGameSetting(String betGame, String roomId) {
        Integer roomIdInt=Integer.valueOf(roomId);
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            if (betGame.equals("pk10")) {
                jsonObject = new JSONObject(gson.toJson(lottery1SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("xyft")) {
                jsonObject = new JSONObject(gson.toJson(lottery2SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("cqssc")) {
                jsonObject = new JSONObject(gson.toJson(lottery3SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("xy28")) {
                jsonObject = new JSONObject(gson.toJson(lottery4SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("ny28")) {
                jsonObject = new JSONObject(gson.toJson(lottery19SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jnd28")) {
                jsonObject = new JSONObject(gson.toJson(lottery5SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jsmt")) {
                jsonObject = new JSONObject(gson.toJson(lottery6SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jssc")) {
                jsonObject = new JSONObject(gson.toJson(lottery7SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jsssc")) {
                jsonObject = new JSONObject(gson.toJson(lottery8SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("kuai3")) {
                jsonObject = new JSONObject(gson.toJson(lottery9SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("bjl")) {
                jsonObject = new JSONObject(gson.toJson(lottery10SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("gd11x5")) {
                jsonObject = new JSONObject(gson.toJson(lottery11SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jssm")) {
                jsonObject = new JSONObject(gson.toJson(lottery12SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("lhc")) {
                jsonObject = new JSONObject(gson.toJson(lottery13SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("jslhc")) {
                jsonObject = new JSONObject(gson.toJson(lottery14SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("twk3")) {
                jsonObject = new JSONObject(gson.toJson(lottery15SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("txffc")) {
                jsonObject = new JSONObject(gson.toJson(lottery16SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("azxy10")) {
                jsonObject = new JSONObject(gson.toJson(lottery17SettingMapper.selectByRoomId(roomIdInt)));
            } else if(betGame.equals("azxy5")) {
                jsonObject = new JSONObject(gson.toJson(lottery18SettingMapper.selectByRoomId(roomIdInt)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
