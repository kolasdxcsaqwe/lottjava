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

    public FetchLotteryResult() {
        pcOrderBeanMapper= TtApplication.getContext().getBean(PCOrderBeanMapper.class);

    }

    public  Object PCSettlement(String gameName, String roomid) {
        Integer roomId=Integer.valueOf(roomid);
        String cnGameName="";
        int openType=0;
        List<PCOrderBean> pcOrderBeanList = pcOrderBeanMapper.selectByStatus("未结算");
        for (PCOrderBean bean : pcOrderBeanList) {
            switch (gameName) {
                case "jnd28":
                    cnGameName="加拿大28";
                    openType=5;
                    break;
                case "xy28":
                    openType = 4;
                    cnGameName = "新加坡28";
                    break;
                case "ny28":
                    openType = 19;
                    cnGameName = "纽约28";
                    break;
            }

//            int sumMoney = pcOrderBeanMapper.sumMoneyByTerm(roomid, )
        }

        return "";
    }

}
