package com.example.tt.Controller;

//七星彩接口

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.Lottery20SettingMapper;
import com.example.tt.dao.RobotUserMapper;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QxcController {

    @Autowired(required = false)
    Lottery20SettingMapper lottery20SettingMapper;

    @ResponseBody
    @RequestMapping(value = "/qxcLotterySetting", method = RequestMethod.POST)
    public Object qxcLotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery20Setting());
    }

    @ResponseBody
    @RequestMapping(value = "/qxcLotterySettingEdit", method = RequestMethod.POST)
    public Object qxcLotterySettingEdit(@RequestParam(name = "roomid") Integer roomid,
                                        @RequestParam(name = "da") Float da,
                                        @RequestParam(name = "xiao") Float xiao,
                                        @RequestParam(name = "dan") Float dan,
                                        @RequestParam(name = "shuang") Float shuang,
                                        @RequestParam(name = "anythree") Float anythree,
                                        @RequestParam(name = "anyfour") Float anyfour,
                                        @RequestParam(name = "frontfourfix") Float frontfourfix,
                                        @RequestParam(name = "endfourfix") Float endfourfix,
                                        @RequestParam(name = "frontfourany") Float frontfourany,
                                        @RequestParam(name = "endfourany") Float endfourany,
                                        @RequestParam(name = "minbet") Float minbet,
                                        @RequestParam(name = "maxbet") Float maxbet) {

        Lottery20Setting lottery20Setting=new Lottery20Setting();
        lottery20Setting.setId(roomid);
        lottery20Setting.setRoomid(roomid);
        lottery20Setting.setDa(da);
        lottery20Setting.setXiao(xiao);
        lottery20Setting.setDan(dan);
        lottery20Setting.setShuang(shuang);
        lottery20Setting.setAnythree(anythree);
        lottery20Setting.setAnyfour(anyfour);
        lottery20Setting.setFrontfourany(frontfourany);
        lottery20Setting.setFrontfourfix(frontfourfix);
        lottery20Setting.setEndfourany(endfourany);
        lottery20Setting.setEndfourfix(endfourfix);
        lottery20Setting.setMinbet(minbet);
        lottery20Setting.setMaxbet(maxbet);

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


}
