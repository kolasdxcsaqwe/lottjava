package com.example.tt.Service;

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.Bean.LotteryOpenBean;
import com.example.tt.Bean.QXCOrder;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.dao.QXCOrderMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QxcService {

    @Autowired(required = false)
    QXCOrderMapper qxcOrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    static SessionStorage sessionStorage=SessionStorage.getInstance();

    public static boolean check(JSONArray jsonArray, int type)
    {
        if(jsonArray.length()<1)
        {
            return false;
        }

        switch (type)
        {
            case 1:
                if(jsonArray.optString(0).length()<3)
                {
                    return false;
                }
                break;
            case 2:
                if(jsonArray.optString(0).length()<4)
                {
                    return false;
                }
                break;
            case 3:
            case 4:
                if(jsonArray.length()!=4)
                {
                    return false;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    if(jsonArray.optString(i).length()<1)
                    {
                        return false;
                    }
                }
                break;
            case 5:
            case 6:
                if(jsonArray.optString(0).length()<4)
                {
                    return false;
                }
                break;
            case 7:
                if(jsonArray.length()<2)
                {
                    return false;
                }
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                if(jsonArray.optString(0).length()<1)
                {
                    return false;
                }
                break;

        }
        return true;
    }



    public Object betQXC(String betArray)
    {
        Lottery20Setting lottery20Setting= LotteryConfigGetter.getInstance().getLottery20Setting();
        int fengTime=lottery20Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean =lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if(!lottery20Setting.getGameopen() || lotteryOpenBean==null || lotteryOpenBean.getNextTime()==null)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if(lotteryOpenBean.getNextTime().getTime()-System.currentTimeMillis()>fengTime*1000)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if(Strings.isEmptyOrNullAmongOf(betArray))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }

        List<QXCOrder> qxcOrderList=new ArrayList<>();

        boolean isFormatOk=true;

        try {
            JSONArray jsonArray=new JSONArray(betArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                boolean isCombineBet=false;
                Integer calculateMoney=0;

                JSONObject jsonObject = jsonArray.optJSONObject(i);
                int orderAmount = jsonObject.optInt("orderAmount", 0);
                int money = jsonObject.optInt("money", 0);
                if (money < 1) {
                    isFormatOk = false;
                }

                String typeCode = jsonObject.optString("typeCode", "");
                GameIndex.QXCGameTypeCode qxcGameTypeCode=GameIndex.QXCGameTypeCode.getQXCGameTypeCode(typeCode);
                if(qxcGameTypeCode==null || qxcGameTypeCode.getCode()>0)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                if (!check(codes,qxcGameTypeCode.getCode()))
                {
                    isFormatOk=false;
                }

                if(!isFormatOk)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                QXCOrder qxcOrder=new QXCOrder();
                qxcOrder.setContent(codes.toString());
                qxcOrder.setTerm(lotteryOpenBean.getNextTerm());


            }
        } catch (Exception e) {
            e.printStackTrace();
            isFormatOk=false;
        }

        if(!isFormatOk)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }
        else
        {

        }



        Map<String,String> map=new HashMap<>();
        map.put("betTerm",lotteryOpenBean.getNextTerm());
        return ReturnDataBuilder.makeBaseJSON(map);
    }

}
