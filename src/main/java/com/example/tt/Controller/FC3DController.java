package com.example.tt.Controller;

import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.Service.FC3DService;
import com.example.tt.Service.PL5Service;
import com.example.tt.utils.ReturnDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "/fc3d")
public class FC3DController {

    @Autowired(required = false)
    FC3DService fc3DService;

    @ResponseBody
    @RequestMapping(value = "/LotterySetting", method = RequestMethod.POST)
    public Object LotterySetting() {
        return ReturnDataBuilder.makeBaseJSON(LotteryConfigGetter.getInstance().getLottery22Setting());
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
