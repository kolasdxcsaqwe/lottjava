package com.example.tt.Controller;

import com.example.tt.Bean.Lottery21Setting;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.Service.FC3DService;
import com.example.tt.Service.PL5Service;
import com.example.tt.dao.Lottery21SettingMapper;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "/fc3d")
public class FC3DController {

    @Autowired(required = false)
    FC3DService fc3DService;

    @Autowired(required = false)
    Lottery21SettingMapper lottery21SettingMapper;

    @ResponseBody
    @RequestMapping(value = "/LotterySetting", method = RequestMethod.POST)
    public Object LotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery21Setting(false));
    }

    @ResponseBody
    @RequestMapping(value = "/LotterySettingEdit", method = RequestMethod.POST)
    public Object LotterySettingEdit(@RequestParam(name = "roomid") Integer roomid,
                                     @RequestParam(name = "dxds",required = false) Float dxds,
                                     @RequestParam(name = "anytwo",required = false) Float anytwo,
                                     @RequestParam(name = "anyone",required = false) Float anyone,
                                     @RequestParam(name = "combinethree",required = false) Float combinethree,
                                     @RequestParam(name = "threefix",required = false) Float threefix,
                                     @RequestParam(name = "combinesix",required = false) Float combinesix,
                                     @RequestParam(name = "onefix",required = false) Float onefix,
                                     @RequestParam(name = "combinethreesum",required = false) Float combinethreesum,
                                     @RequestParam(name = "combinesixsum",required = false) Float combinesixsum,
                                     @RequestParam(name = "fronttwofix",required = false) Float fronttwofix,
                                     @RequestParam(name = "backtwofix",required = false) Float backtwofix,
                                     @RequestParam(name = "minbet",required = false) Float minbet,
                                     @RequestParam(name = "maxbet",required = false) Float maxbet,
                                     @RequestParam(name = "gameopen",required = false) String gameopen,
                                     @RequestParam(name = "fengtime",required = false) Integer fengtime,
                                     @RequestParam(name = "rules",required = false) String rules) {

        Lottery21Setting lottery21Setting=new Lottery21Setting();
        lottery21Setting.setId(roomid);
        lottery21Setting.setRoomid(roomid);
        lottery21Setting.setCombinethree(combinethree);
        lottery21Setting.setCombinesix(combinesix);
        lottery21Setting.setCombinethreesum(combinethreesum);
        lottery21Setting.setCombinesixsum(combinesixsum);
        lottery21Setting.setFronttwofix(fronttwofix);
        lottery21Setting.setBacktwofix(backtwofix);
        lottery21Setting.setDxds(dxds);
        lottery21Setting.setAnyone(anyone);
        lottery21Setting.setAnytwo(anytwo);
        lottery21Setting.setThreefix(threefix);
        lottery21Setting.setOnefix(onefix);
        lottery21Setting.setMinbet(minbet);
        lottery21Setting.setMaxbet(maxbet);
        lottery21Setting.setRules(rules);
        if(!Strings.isEmptyOrNullAmongOf(gameopen))
        {
            switch (gameopen.toLowerCase())
            {
                case "on":
                    lottery21Setting.setGameopen(true);
                    break;
                case "off":
                    lottery21Setting.setGameopen(false);
                    break;
            }
        }
        lottery21Setting.setFengtime(fengtime);


        int status=lottery21SettingMapper.updateOrInsertById(lottery21Setting);
        if(status>0)
        {
            if(!lottery21Setting.getGameopen())
            {
                LotteryConfigGetter.getInstance().getLottery21Setting(true);
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
    public String cancelOrder(@RequestParam(name = "id") String id, HttpServletRequest request) {
        return fc3DService.cancelOrder(id);
    }

    //获取开奖结果 和结算
    @ResponseBody
    @RequestMapping(value = "/fetchResult", method = RequestMethod.GET)
    public Object fetchResult(@RequestParam(name = "roomId") String roomId,HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":");
        sb.append(request.getServerPort());

        return fc3DService.fetchFC3DResult(roomId,sb.toString());
    }



}
