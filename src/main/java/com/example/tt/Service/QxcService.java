package com.example.tt.Service;

import com.example.tt.Bean.Lottery20Setting;
import com.example.tt.Bean.LotteryOpenBean;
import com.example.tt.Bean.QXCOrder;
import com.example.tt.Bean.UserBean;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.dao.QXCOrderMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QxcService {

    @Autowired(required = false)
    QXCOrderMapper qxcOrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    static SessionStorage sessionStorage=SessionStorage.getInstance();

    public static int check(JSONArray jsonArray, int type)
    {
        int orderAmount=0;

        if(jsonArray==null || jsonArray.length()<1)
        {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul=1;
        List<String> stringList=new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String str= jsonArray.optString(i).trim().replaceAll(" ","");
            if(str.length()<1 ||!Strings.isDigitOnly(str))
            {
                return 0;
            }
            else
            {
                stringList.add(str);
                mul=mul*str.length();
            }
        }

        switch (type)
        {
            case 1:
                orderAmount=calculateOrderAnyChoose(stringList.get(0).length(),3);
                break;
            case 2:
                orderAmount=calculateOrderAnyChoose(stringList.get(0).length(),2);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                orderAmount=mul;
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                orderAmount=1;
                break;

        }

        return orderAmount;
    }

    private float getWinRate(int gameType,Lottery20Setting lottery20Setting)
    {
        float rate=0.0f;
        switch (gameType)
        {
            case 1:
                rate=lottery20Setting.getAnythree();
                break;
            case 2:
                rate=lottery20Setting.getAnytwo();
                break;
            case 3:
                rate=lottery20Setting.getFourfix();
                break;
            case 4:
                rate=lottery20Setting.getThreefix();
                break;
            case 5:
                rate=lottery20Setting.getTwofix();
                break;
            case 6:
                rate=lottery20Setting.getOnefix();
                break;
            case 7:
                rate=lottery20Setting.getTouweifix();
                break;
            case 8:
                rate=lottery20Setting.getDa();
                break;
            case 9:
                rate=lottery20Setting.getXiao();
                break;
            case 10:
                rate=lottery20Setting.getDan();
                break;
            case 11:
                rate=lottery20Setting.getShuang();
                break;
        }
        return rate;
    }

    public Object betQXC(String betArray,String userId,String roomId)
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

        if(!Strings.isDigitOnly(roomId))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if(Strings.isEmptyOrNullAmongOf(betArray))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }

        List<QXCOrder> qxcOrderList=new ArrayList<>();

        boolean isFormatOk=true;

        //一单 可以有n注 一注可以是n元 ,大于1注就是复式
        try {
            JSONArray jsonArray=new JSONArray(betArray);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.optJSONObject(i);

                String gamName = jsonObject.optString("gamName", "");
                int orderPrice = jsonObject.optInt("orderPrice", 0);
                if (orderPrice < 1 || Strings.isEmptyOrNullAmongOf(gamName)) {
                    isFormatOk = false;
                }

                GameIndex.QXCGameTypeCode qxcGameTypeCode=GameIndex.QXCGameTypeCode.getQXCGameTypeCode(gamName);
                if(qxcGameTypeCode==null)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount=check(codes,qxcGameTypeCode.getCode());
                if (orderAmount<1)
                {
                    isFormatOk=false;
                }

                if(orderAmount>1 && orderPrice%orderAmount!=0)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S15);
                }

                if(!isFormatOk)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                UserBean userBean=LotteryConfigGetter.getInstance().getUser(userId,Integer.parseInt(roomId));
                float winRate=getWinRate(qxcGameTypeCode.getCode(),lottery20Setting);
                QXCOrder qxcOrder=new QXCOrder();
                qxcOrder.setAddtime(Calendar.getInstance().getTime());
                qxcOrder.setContent(codes.toString());
                qxcOrder.setTerm(lotteryOpenBean.getNextTerm());
                qxcOrder.setMoney(orderPrice * orderAmount);
                qxcOrder.setStatus(0);
                qxcOrder.setGamename(gamName);
                qxcOrder.setGameType(qxcGameTypeCode.getCode());
                qxcOrder.setOrderamount(orderAmount);
                qxcOrder.setUnitprice(orderPrice);
                qxcOrder.setWinrate(winRate);
                qxcOrder.setUsername(userBean.getUsername());
                qxcOrder.setHeadimg(userBean.getHeadimg());
                qxcOrder.setMingci("");
                qxcOrder.setJia(userBean.getJia());
                int status=qxcOrderMapper.insertSelective(qxcOrder);
                if(status<1)
                {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isFormatOk=false;
        }

        if(!isFormatOk)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }


        Map<String,String> map=new HashMap<>();
        map.put("betTerm",lotteryOpenBean.getNextTerm());
        return ReturnDataBuilder.makeBaseJSON(map);
    }


    //choose 用户选中 当前彩种任选几
    private static int  calculateOrderAnyChoose(int choose, int need)
    {
        int n=1;
        int nm=1;
        int m=1;

        for (int i = 0; i < choose; i++) {
            n=n*(i+1);
            if(choose-need-i>0)
            {
                nm=nm*(choose-need-i);
            }
            if(need-i>0)
            {
                m=m*(need-i);
            }
        }
        return n/nm/m;
    }
}
