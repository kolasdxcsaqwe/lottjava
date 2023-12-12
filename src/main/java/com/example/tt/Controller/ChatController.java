package com.example.tt.Controller;

import com.example.tt.Bean.*;
import com.example.tt.OpenResult.LotteryConfigGetter;
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
    BanWordsMapper banWordsMapper;

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
        List<ChatBean> list=new ArrayList<>();
        int nowTerm =0;
        if(!Strings.isEmptyOrNullAmongOf(game) && game.equals("all"))
        {
            list.addAll(chatBeanMapper.last50RowByRoom(Integer.parseInt(roomid)));
        }
        else
        {
            list.addAll(chatBeanMapper.last50RowByGame(Integer.parseInt(roomid),game));
            nowTerm = GameIndex.getLotteryIndex(game);
            if (nowTerm < 1) {
                MyLog.e("gameName错误--->"+game);
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1);
            }
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
            userBean = userBeanMapper.selectByUserId(userid,Integer.parseInt(roomid));

            if (userBean == null) {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S3);
            } else {
                userBeanHashMap.put(userid, userBean);
            }
        }

        GameSettingShortBean gameSettingShortBean =getGameSetting(game);
        Integer closeLimitTime=gameSettingShortBean.getFengTime();//提前封盘的时间

        boolean isGameOpening=true;//该彩种是否开启了
        if(gameSettingShortBean.getGameOpen().equals("false"))
        {
            isGameOpening=false;
        }

        if(!isGameOpening)
        {
            //未开盘
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S6);
        }

        LotteryOpenBean lotteryOpenBean=lotteryOpenBeanMapper.getLastOpenData(GameIndex.getLotteryIndex(game));
        String termForNow=lotteryOpenBean.getNextTerm();
        boolean isAvailableBet=(lotteryOpenBean.getNextTime().getTime()-System.currentTimeMillis())/1000<closeLimitTime;

        if(!isAvailableBet)
        {
            //截至投注
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S6);
        }

        BanWords user=banWordsMapper.selectBanUser(Integer.parseInt(roomid),userid);
        if(user!=null && !Strings.isEmptyOrNullAmongOf(user.getUsername()))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S7);
        }

        LotteryRoomSetting lotteryRoomSetting= LotteryConfigGetter.getInstance().getLotteryRoomSetting();
        String words[]=content.split("\\|");
        for (int i = 0; i <words.length ; i++) {
            if(Strings.isEmptyOrNullAmongOf(words[i]))
            {
                continue;
            }

            if(content.contains(words[i]))
            {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S8);
            }
        }

        boolean isAdminBroadcast=content.startsWith("@");


        return ReturnDataBuilder.makeBaseJSON(null);
    }



    private GameSettingShortBean getGameSetting(String betGame) {
        GameSettingShortBean bean = null;

        if (betGame.equals("pk10")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery1Setting());
        } else if(betGame.equals("xyft")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery2Setting());
        } else if(betGame.equals("cqssc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery3Setting());
        } else if(betGame.equals("xy28")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery4Setting());
        } else if(betGame.equals("ny28")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery19Setting());
        } else if(betGame.equals("jnd28")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery5Setting());
        } else if(betGame.equals("jsmt")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery6Setting());
        } else if(betGame.equals("jssc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery7Setting());
        } else if(betGame.equals("jsssc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery8Setting());
        } else if(betGame.equals("kuai3")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery9Setting());
        } else if(betGame.equals("bjl")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery10Setting());
        } else if(betGame.equals("gd11x5")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery11Setting());
        } else if(betGame.equals("jssm")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery12Setting());
        } else if(betGame.equals("lhc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery13Setting());
        } else if(betGame.equals("jslhc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery14Setting());
        } else if(betGame.equals("twk3")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery15Setting());
        } else if(betGame.equals("txffc")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery16Setting());
        } else if(betGame.equals("azxy10")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery17Setting());
        } else if(betGame.equals("azxy5")) {
            bean = GameSettingShortBean.beanConverter(LotteryConfigGetter.getInstance().getLottery18Setting());
        }
        return bean;
    }

}
