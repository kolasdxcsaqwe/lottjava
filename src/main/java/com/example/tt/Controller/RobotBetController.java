package com.example.tt.Controller;

import com.example.tt.Bean.RobotPlans;
import com.example.tt.Bean.RobotUser;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.Lottery19SettingMapper;
import com.example.tt.dao.RobotPlansMapper;
import com.example.tt.dao.RobotUserMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class RobotBetController {

    @Autowired(required = false)
    RobotUserMapper robotUserMapper;

    static ScheduledExecutorService threadPool = Executors
            .newScheduledThreadPool(3);

    @ResponseBody
    @RequestMapping(value = "/robotBet", method = RequestMethod.POST)
    public Object robotBet(@RequestParam(name = "betPeriod") int betPeriod,
                           @RequestParam(name = "delay") int delay,
                           @RequestParam(name = "game") String game,
                           @RequestParam(name = "roomid") String roomid) {

        if (Strings.isNullAmongOf(game, roomid) || !Strings.isDigitOnly(roomid)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2).toString();
        }

        Gson gson=new Gson();
        List<RobotUser> robotUsers=null;
        String json=RedisCache.getInstance().get(game+roomid);
        if(Strings.isEmptyOrNullAmongOf(json))
        {
            robotUsers= robotUserMapper.selectRobotsByGame(roomid,game);
            if(robotUsers.size()!=0)
            {
                RedisCache.getInstance().set(game+roomid,gson.toJson(robotUsers),1800*1000);
            }
        }
        else
        {
            try {
                JSONArray jsonArray=new JSONArray(json);
                if(jsonArray.length()==0)
                {
                    robotUsers= robotUserMapper.selectRobotsByGame(roomid,game);
                    if(robotUsers.size()!=0)
                    {
                        RedisCache.getInstance().set(game+roomid,gson.toJson(robotUsers),1800*1000);
                    }
                }
                else
                {
                    robotUsers=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.optJSONObject(i);
                        RobotUser robotUser=new RobotUser();
                        robotUser.setGame(jsonObject.optString("game",""));
                        robotUser.setHeadimg(jsonObject.optString("headimg",""));
                        robotUser.setId(jsonObject.optInt("id",1));
                        robotUser.setName(jsonObject.optString("name",""));
                        robotUser.setRare(jsonObject.optInt("rare",80));
                        robotUser.setUserid(jsonObject.optString("userid",""));
                        robotUser.setRunstatus(jsonObject.optInt("runstatus",1));
                        robotUser.setPlan(jsonObject.optString("plan",""));
                        robotUser.setRoomid(jsonObject.optString("roomid",""));
                        robotUsers.add(robotUser);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < robotUsers.size(); i++) {
            String planStr=robotUsers.get(i).getPlan();
            String headImg=robotUsers.get(i).getHeadimg();
            String name=robotUsers.get(i).getName();
            Integer rare=robotUsers.get(i).getRare();
            RobotUser robotUser=robotUsers.get(i);

            if(Strings.isEmptyOrNullAmongOf(planStr,headImg,name))
            {
                continue;
            }

            String plans[]=planStr.split("\\|");
            for (int j = 0; j < plans.length; j++) {
                int random=(int)(Math.random()*10)+1;//1-10之间
                boolean isContinue=random<rare/10;
                if(isContinue)
                {
                    RobotPlans robotPlans= LotteryConfigGetter.getInstance().getRobotPlans().get(Integer.parseInt(plans[j]));
                    threadPool.schedule(new RobotRunnable(robotPlans,robotUser,betPeriod),delay, TimeUnit.MILLISECONDS);
                }
            }

        }

        return ReturnDataBuilder.makeBaseJSON(null);
    }
}
