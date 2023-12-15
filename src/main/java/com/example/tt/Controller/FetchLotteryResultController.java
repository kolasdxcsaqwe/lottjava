package com.example.tt.Controller;

import com.example.tt.Bean.PCOrderBean;
import com.example.tt.Service.QxcService;
import com.example.tt.dao.*;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class FetchLotteryResultController {

    @Autowired(required = false)
    QxcService qxcService;

    //获取单个彩种的倒计时和用户资料
    @ResponseBody
    @RequestMapping(value = "/fetchUserWithCountDown", method = RequestMethod.POST)
    public Object QXCSendChat(@RequestParam(name = "userId") String userId,@RequestParam(name = "gameName") String gameName,
                              @RequestParam(name = "roomId") String roomId, HttpServletRequest request) {

        if(Strings.isEmptyOrNullAmongOf(userId,gameName,roomId) || !Strings.isDigitOnly(roomId))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        int gameCode=GameIndex.getLotteryIndex(gameName);
        if(gameCode<0)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1);
        }

        switch (gameCode)
        {
            case 20:
                //七星
                return qxcService.getRemainTimeAndUser(userId,roomId,request);
        }

        return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
    }
}

