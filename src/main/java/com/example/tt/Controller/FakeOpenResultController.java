package com.example.tt.Controller;

import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.utils.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FakeOpenResultController {

    int period=60000;
    int startQXCTerm=23144;
    long startTime=1702781820000l;


    @ResponseBody
    @RequestMapping(value = "/fakeOpenResult")
    public Object fakeOpenResult(@RequestParam(name = "lotteryName") String lotteryName) {

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("errorCode",0);
            jsonObject.put("errorCode",0);
            JSONObject result=new JSONObject();
            JSONObject data=new JSONObject();
            jsonObject.put("result",result);
            result.put("data",data);

            int type=GameIndex.getLotteryIndex(lotteryName);
            long addTerm=(System.currentTimeMillis()-startTime)/period;

            data.put("preDrawIssue",startQXCTerm+addTerm);
            data.put("preDrawCode",random(7));
            data.put("preDrawTime", TimeUtils.convertToDetailTime(addTerm*period+ startTime));
            data.put("drawTime",TimeUtils.convertToDetailTime((addTerm+1)*period+ startTime));
            if(type>0) {
                switch (type)
                {
                    case 20:
                        data.put("preDrawCode",random(7));
                        return jsonObject.toString();
                    case 21:
                        data.put("preDrawCode",random(3));
                        break;
                    case 22:
                        data.put("preDrawCode",random(5));
                        break;
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
    }


    public String random(int amount)
    {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < amount; i++) {
            int random = (int) (Math.random() * 10);
            sb.append(random);
            sb.append(",");
        }
        return sb.toString();
    }
}
