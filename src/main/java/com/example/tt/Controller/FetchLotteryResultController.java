package com.example.tt.Controller;

import com.example.tt.Bean.PCOrderBean;
import com.example.tt.Service.FC3DService;
import com.example.tt.Service.PL5Service;
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
@CrossOrigin
public class FetchLotteryResultController {

    @Autowired(required = false)
    QxcService qxcService;

    @Autowired(required = false)
    PL5Service pl5Service;

    @Autowired(required = false)
    FC3DService fc3DService;

    //获取单个彩种的倒计时和用户资料
    @ResponseBody
    @RequestMapping(value = "/fetchUserWithCountDown", method = RequestMethod.POST)
    public Object fetchUserWithCountDown(@RequestParam(name = "userId") String userId,@RequestParam(name = "gameName") String gameName,
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
                //七星彩
                return qxcService.getRemainTimeAndUser(userId,roomId,request);
            case 22:
                //排列5
                return pl5Service.getRemainTimeAndUser(userId,roomId,request);
            case 21:
                //福彩3D
                return fc3DService.getRemainTimeAndUser(userId,roomId,request);
        }

        return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
    }


    //获取单个彩种的倒计时
    @ResponseBody
    @RequestMapping(value = "/fetchOpenTime", method = RequestMethod.POST)
    public Object fetchOpenTime(@RequestParam(name = "userId") String userId,@RequestParam(name = "gameName") String gameName,
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
                //七星彩
                return qxcService.getRemainTime(userId,roomId,request);
            case 22:
                //排列5
                return pl5Service.getRemainTime(userId,roomId,request);
            case 21:
                //福彩3D
                return fc3DService.getRemainTime(userId,roomId,request);
        }

        return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
    }
}

