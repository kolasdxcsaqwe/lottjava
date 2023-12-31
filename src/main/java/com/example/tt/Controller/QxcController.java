package com.example.tt.Controller;

//七星彩接口

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.Bean.LotteryOpenBean;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.Service.QxcService;
import com.example.tt.dao.Lottery20SettingMapper;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "/qxc")
public class QxcController {

    @Autowired(required = false)
    Lottery20SettingMapper lottery20SettingMapper;

    @Autowired(required = false)
    QxcService qxcService;

    //<p>11.54%  15.6  7.3 任意3</p>
    //         <p>4.577% 39.32  19.66 任意4</p>
    //         <p>0.026%  19600  9800 前4定位</p>
    //         <p>0.026%  19600  9800 后4定位</p>
    //         <p>1.81%  99.44  49.72 前4任意</p>
    //         <p>1.81%  99.44  49.72 后4任意</p>
    //         <p>1.093%  164.68  82.34 头尾定位</p>

    @ResponseBody
    @RequestMapping(value = "/LotterySetting", method = RequestMethod.POST)
    public Object LotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery20Setting(false));
    }

    @ResponseBody
    @RequestMapping(value = "/LotterySettingEdit", method = RequestMethod.POST)
    public Object LotterySettingEdit(@RequestParam(name = "roomid") Integer roomid,
                                        @RequestParam(name = "dxds",required = false) Float dxds,
                                        @RequestParam(name = "anytwo",required = false) Float anytwo,
                                        @RequestParam(name = "anythree",required = false) Float anythree,
                                        @RequestParam(name = "fourfix",required = false) Float fourfix,
                                        @RequestParam(name = "threefix",required = false) Float threefix,
                                        @RequestParam(name = "twofix",required = false) Float twofix,
                                        @RequestParam(name = "onefix",required = false) Float onefix,
                                        @RequestParam(name = "touweifix",required = false) Float touweifix,
                                        @RequestParam(name = "minbet",required = false) Float minbet,
                                        @RequestParam(name = "maxbet",required = false) Float maxbet,
                                        @RequestParam(name = "gameopen",required = false) String gameopen,
                                        @RequestParam(name = "fengtime",required = false) Integer fengtime,
                                        @RequestParam(name = "rules",required = false) String rules) {

        Lottery20Setting lottery20Setting=new Lottery20Setting();
        lottery20Setting.setId(roomid);
        lottery20Setting.setRoomid(roomid);
        lottery20Setting.setDxds(dxds);
        lottery20Setting.setAnythree(anythree);
        lottery20Setting.setAnytwo(anytwo);
        lottery20Setting.setFourfix(fourfix);
        lottery20Setting.setThreefix(threefix);
        lottery20Setting.setTwofix(twofix);
        lottery20Setting.setOnefix(onefix);
        lottery20Setting.setMinbet(minbet);
        lottery20Setting.setMaxbet(maxbet);
        lottery20Setting.setRules(rules);
        lottery20Setting.setTouweifix(touweifix);
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


        int status=lottery20SettingMapper.updateByPrimaryKeySelective(lottery20Setting);

        if(status>0)
        {
            if(lottery20Setting.getGameopen()!=null)
            {
                LotteryConfigGetter.getInstance().getLottery20Setting(true);
            }

            return ReturnDataBuilder.makeBaseJSON(null);
        }
        else
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }

    }

    //获取开奖结果 和结算
    @ResponseBody
    @RequestMapping(value = "/fetchResult", method = RequestMethod.GET)
    public Object fetchResult(@RequestParam(name = "roomId") String roomId,HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":");
        sb.append(request.getServerPort());

        return qxcService.fetchQXCResult(roomId,sb.toString());
    }

    //下注撤单
    @ResponseBody
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelOrder(@RequestParam(name = "id") String id,
                              @RequestParam(name = "userId") String userId,HttpServletRequest request) {
        return qxcService.cancelOrder(id,userId);
    }
}
