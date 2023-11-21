package com.example.tt.OpenResult;

import com.example.tt.Bean.PCOrderBean;
import com.example.tt.TtApplication;
import com.example.tt.dao.*;
import com.example.tt.utils.JSONException;
import com.example.tt.utils.JSONObject;
import com.example.tt.utils.ReturnDataBuilder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FetchLotteryResult {

    PCOrderBeanMapper pcOrderBeanMapper;

    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    Lottery1SettingMapper lottery1SettingMapper;

    Lottery2SettingMapper lottery2SettingMapper;

    Lottery3SettingMapper lottery3SettingMapper;

    Lottery4SettingMapper lottery4SettingMapper;

    Lottery5SettingMapper lottery5SettingMapper;

    Lottery6SettingMapper lottery6SettingMapper;

    Lottery7SettingMapper lottery7SettingMapper;

    Lottery8SettingMapper lottery8SettingMapper;

    Lottery9SettingMapper lottery9SettingMapper;

    Lottery10SettingMapper lottery10SettingMapper;

    Lottery11SettingMapper lottery11SettingMapper;

    Lottery12SettingMapper lottery12SettingMapper;

    Lottery13SettingMapper lottery13SettingMapper;

    Lottery14SettingMapper lottery14SettingMapper;

    Lottery15SettingMapper lottery15SettingMapper;

    Lottery16SettingMapper lottery16SettingMapper;

    Lottery17SettingMapper lottery17SettingMapper;

    Lottery18SettingMapper lottery18SettingMapper;

    Lottery19SettingMapper lottery19SettingMapper;

    private static FetchLotteryResult result;

    public  static FetchLotteryResult getInstance() {
        if (result == null) {
            result =new FetchLotteryResult();
        }
        return result;
    }

    public FetchLotteryResult() {
        pcOrderBeanMapper= TtApplication.context.getBean(PCOrderBeanMapper.class);
        lotteryOpenBeanMapper= TtApplication.context.getBean(LotteryOpenBeanMapper.class);
        lottery1SettingMapper= TtApplication.context.getBean(Lottery1SettingMapper.class);
        lottery2SettingMapper= TtApplication.context.getBean(Lottery2SettingMapper.class);
        lottery3SettingMapper= TtApplication.context.getBean(Lottery3SettingMapper.class);
        lottery4SettingMapper= TtApplication.context.getBean(Lottery4SettingMapper.class);
        lottery5SettingMapper= TtApplication.context.getBean(Lottery5SettingMapper.class);
        lottery6SettingMapper= TtApplication.context.getBean(Lottery6SettingMapper.class);
        lottery7SettingMapper= TtApplication.context.getBean(Lottery7SettingMapper.class);
        lottery8SettingMapper= TtApplication.context.getBean(Lottery8SettingMapper.class);
        lottery9SettingMapper= TtApplication.context.getBean(Lottery9SettingMapper.class);
        lottery10SettingMapper= TtApplication.context.getBean(Lottery10SettingMapper.class);
        lottery11SettingMapper= TtApplication.context.getBean(Lottery11SettingMapper.class);
        lottery12SettingMapper= TtApplication.context.getBean(Lottery12SettingMapper.class);
        lottery13SettingMapper= TtApplication.context.getBean(Lottery13SettingMapper.class);
        lottery14SettingMapper= TtApplication.context.getBean(Lottery14SettingMapper.class);
        lottery15SettingMapper= TtApplication.context.getBean(Lottery15SettingMapper.class);
        lottery16SettingMapper= TtApplication.context.getBean(Lottery16SettingMapper.class);
        lottery17SettingMapper= TtApplication.context.getBean(Lottery17SettingMapper.class);
        lottery18SettingMapper= TtApplication.context.getBean(Lottery18SettingMapper.class);
        lottery19SettingMapper= TtApplication.context.getBean(Lottery19SettingMapper.class);

    }

    public  Object PCSettlement(String gameName, String roomid) {
        Integer roomId=Integer.valueOf(roomid);
        List<PCOrderBean> pcOrderBeanList = pcOrderBeanMapper.selectByStatus("未结算");
        for (PCOrderBean bean : pcOrderBeanList) {
            Gson gson = new Gson();
            JSONObject jsonObject = null;
            try {
                switch (gameName) {
                    case "jnd28":
                        jsonObject = new JSONObject(gson.toJson(lottery5SettingMapper.selectByRoomId(roomId)));
                        break;
                    case "xy28":
                        jsonObject = new JSONObject(gson.toJson(lottery4SettingMapper.selectByRoomId(roomId)));
                        break;
                    case "ny28":
                        jsonObject = new JSONObject(gson.toJson(lottery19SettingMapper.selectByRoomId(roomId)));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1).toString();
            }

//            int sumMoney = pcOrderBeanMapper.sumMoneyByTerm(roomid, )
        }

        return "";
    }

}
