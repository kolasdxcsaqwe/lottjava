package com.example.tt.Controller;

import com.example.tt.Bean.Lottery22Setting;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.Service.PL5Service;
import com.example.tt.dao.Lottery20SettingMapper;
import com.example.tt.dao.Lottery22SettingMapper;
import com.example.tt.utils.GameIndex;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "/pl5")
public class PL5Controller {

    @Autowired(required = false)
    Lottery22SettingMapper lottery22SettingMapper;

    @Autowired(required = false)
    PL5Service pl5Service;

    @ResponseBody
    @RequestMapping(value = "/LotterySetting", method = RequestMethod.POST)
    public Object LotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery22Setting(false));
    }

    @ResponseBody
    @RequestMapping(value = "/LotterySettingEdit", method = RequestMethod.POST)
    public Object LotterySettingEdit(@RequestParam(name = "roomid") Integer roomid,
                                        @RequestParam(name = "dxds",required = false) Float dxds,
                                        @RequestParam(name = "anytwo",required = false) Float anytwo,
                                        @RequestParam(name = "anythree",required = false) Float anythree,
                                        @RequestParam(name = "fivefix",required = false) Float fivefix,
                                        @RequestParam(name = "threefix",required = false) Float threefix,
                                        @RequestParam(name = "twofix",required = false) Float twofix,
                                        @RequestParam(name = "onefix",required = false) Float onefix,
                                        @RequestParam(name = "youniu",required = false) Float youniu,
                                        @RequestParam(name = "wuniu",required = false) Float wuniu,
                                        @RequestParam(name = "minbet",required = false) Float minbet,
                                        @RequestParam(name = "maxbet",required = false) Float maxbet,
                                        @RequestParam(name = "gameopen",required = false) String gameopen,
                                        @RequestParam(name = "fengtime",required = false) Integer fengtime,
                                        @RequestParam(name = "rules",required = false) String rules) {

        Lottery22Setting lottery22Setting=new Lottery22Setting();
        lottery22Setting.setId(roomid);
        lottery22Setting.setRoomid(roomid);
        lottery22Setting.setDxds(dxds);
        lottery22Setting.setAnythree(anythree);
        lottery22Setting.setAnytwo(anytwo);
        lottery22Setting.setFivefix(fivefix);
        lottery22Setting.setThreefix(threefix);
        lottery22Setting.setTwofix(twofix);
        lottery22Setting.setOnefix(onefix);
        lottery22Setting.setMinbet(minbet);
        lottery22Setting.setMaxbet(maxbet);
        lottery22Setting.setRules(rules);
        lottery22Setting.setYouniu(youniu);
        lottery22Setting.setWuniu(wuniu);



        if(!Strings.isEmptyOrNullAmongOf(gameopen))
        {
            switch (gameopen.toLowerCase())
            {
                case "on":
                    lottery22Setting.setGameopen(true);
                    break;
                case "off":
                    lottery22Setting.setGameopen(false);
                    break;
            }

        }


        lottery22Setting.setFengtime(fengtime);

        int status=lottery22SettingMapper.updateByPrimaryKeySelective(lottery22Setting);

        if(status>0)
        {
            if(lottery22Setting.getGameopen()!=null)
            {
                LotteryConfigGetter.getInstance().getLottery22Setting(true);
            }

            return ReturnDataBuilder.makeBaseJSON(null);
        }
        else
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }

    }

    //下注撤单
    @ResponseBody
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelOrder(@RequestParam(name = "id") String id,
                              @RequestParam(name = "userId") String userId,HttpServletRequest request) {
        return pl5Service.cancelOrder(id,userId);
    }

    //获取开奖结果 和结算
    @ResponseBody
    @RequestMapping(value = "/fetchResult", method = RequestMethod.GET)
    public Object fetchResult(@RequestParam(name = "roomId") String roomId,HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":");
        sb.append(request.getServerPort());

        return pl5Service.fetchPL5Result(roomId,sb.toString());
    }

}
