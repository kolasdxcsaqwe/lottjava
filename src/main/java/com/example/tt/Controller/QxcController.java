package com.example.tt.Controller;

//七星彩接口

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.Bean.LotteryOpenBean;
import com.example.tt.FormatCheck.QXCBetFormatChecker;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.Lottery20SettingMapper;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.dao.RobotUserMapper;
import com.example.tt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class QxcController {

    @Autowired(required = false)
    Lottery20SettingMapper lottery20SettingMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    //<p>11.54%  15.6  7.3 任意3</p>
    //         <p>4.577% 39.32  19.66 任意4</p>
    //         <p>0.026%  19600  9800 前4定位</p>
    //         <p>0.026%  19600  9800 后4定位</p>
    //         <p>1.81%  99.44  49.72 前4任意</p>
    //         <p>1.81%  99.44  49.72 后4任意</p>
    //         <p>1.093%  164.68  82.34 头尾定位</p>

    @ResponseBody
    @RequestMapping(value = "/qxcLotterySetting", method = RequestMethod.POST)
    public Object qxcLotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery20Setting());
    }

    @ResponseBody
    @RequestMapping(value = "/qxcLotterySettingEdit", method = RequestMethod.POST)
    public Object qxcLotterySettingEdit(@RequestParam(name = "roomid") Integer roomid,
                                        @RequestParam(name = "dxds",required = false) Float dxds,
                                        @RequestParam(name = "anythree",required = false) Float anythree,
                                        @RequestParam(name = "anyfour",required = false) Float anyfour,
                                        @RequestParam(name = "frontfourfix",required = false) Float frontfourfix,
                                        @RequestParam(name = "endfourfix",required = false) Float endfourfix,
                                        @RequestParam(name = "frontfourany",required = false) Float frontfourany,
                                        @RequestParam(name = "endfourany",required = false) Float endfourany,
                                        @RequestParam(name = "minbet",required = false) Float minbet,
                                        @RequestParam(name = "maxbet",required = false) Float maxbet,
                                        @RequestParam(name = "gameopen",required = false) String gameopen,
                                        @RequestParam(name = "fengtime",required = false) Integer fengtime,
                                        @RequestParam(name = "rules",required = false) String rules) {

        Lottery20Setting lottery20Setting=new Lottery20Setting();
        lottery20Setting.setId(roomid);
        lottery20Setting.setRoomid(roomid);
        lottery20Setting.setDa(dxds);
        lottery20Setting.setXiao(dxds);
        lottery20Setting.setDan(dxds);
        lottery20Setting.setShuang(dxds);
        lottery20Setting.setAnythree(anythree);
        lottery20Setting.setAnyfour(anyfour);
        lottery20Setting.setFrontfourany(frontfourany);
        lottery20Setting.setFrontfourfix(frontfourfix);
        lottery20Setting.setEndfourany(endfourany);
        lottery20Setting.setEndfourfix(endfourfix);
        lottery20Setting.setMinbet(minbet);
        lottery20Setting.setMaxbet(maxbet);
        lottery20Setting.setRules(rules);
        if(!Strings.isEmptyOrNullAmongOf(gameopen))
        {
            switch (gameopen.toLowerCase())
            {
                case "on":
                    lottery20Setting.setGameopen(true);
                    break;
                case "off":
                    lottery20Setting.setGameopen(false);
                    break;
            }
        }
        lottery20Setting.setFengtime(fengtime);


        int status=lottery20SettingMapper.updateOrInsertById(lottery20Setting);
        if(status>0)
        {
            return ReturnDataBuilder.makeBaseJSON(null);
        }
        else
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/QXCSendChat", method = RequestMethod.POST)
    public Object QXCSendChat(@RequestParam(name = "betArray") String betArray) {

        Lottery20Setting lottery20Setting= LotteryConfigGetter.getInstance().getLottery20Setting();
        int fengTime=lottery20Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean =lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if(!lottery20Setting.getGameopen() || lotteryOpenBean==null || lotteryOpenBean.getNextTime()==null)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if(lotteryOpenBean.getNextTime().getTime()-System.currentTimeMillis()>fengTime*1000)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if(Strings.isEmptyOrNullAmongOf(betArray))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }

        boolean isFormatOk=true;
        try {

            JSONArray jsonArray=new JSONArray(betArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                int orderAmount = jsonObject.optInt("orderAmount", 0);
                int money = jsonObject.optInt("money", 0);
                if (money < 1) {
                    isFormatOk = false;
                }

                int type = jsonObject.optInt("type", -1);

                JSONArray codes = jsonObject.optJSONArray("codes");
                if (!QXCBetFormatChecker.check(codes))
                {
                    isFormatOk=false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isFormatOk=false;
        }

        if(!isFormatOk)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }


        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery20Setting());
    }
}
