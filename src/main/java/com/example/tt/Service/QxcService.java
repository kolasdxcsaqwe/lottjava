package com.example.tt.Service;

import com.example.tt.Bean.*;
import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.OpenResult.FixChooseCalWin;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.dao.MarkLogMapper;
import com.example.tt.dao.QXCOrderMapper;
import com.example.tt.dao.UserBeanMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QxcService {

    @Autowired(required = false)
    QXCOrderMapper qxcOrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    @Autowired(required = false)
    MarkLogMapper markLogMapper;

    static SessionStorage sessionStorage = SessionStorage.getInstance();
    final String qxcUrl="https://api.api68.com/QuanGuoCai/getLotteryInfo.do";//七星彩开奖地址

    public static int check(JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        List<String> stringList = new ArrayList<>();
        Map<Integer,String> codes=new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject=jsonArray.optJSONObject(i);
            String str = jsonObject.optString("code","").trim().replaceAll(" ", "");
            codes.put(jsonObject.optInt("pos",-1),str);
            if (str.length() < 1 || !Strings.isDigitOnly(str)) {
                return 0;
            } else {
                stringList.add(str);
                mul = mul * str.length();
            }
        }

        switch (type) {
            case 1:
                orderAmount = calculateOrderAnyChoose(codes.get(0).length(), 3);
                break;
            case 2:
                orderAmount = calculateOrderAnyChoose(codes.get(0).length(), 2);
                break;
            case 3:
                orderAmount=checkFormat(codes,0,1,2,3)?mul:0;
                break;
            case 4:
                orderAmount=checkFormat(codes,0,1,2)?mul:0;
                break;
            case 5:
                orderAmount=checkFormat(codes,0,1)?mul:0;
                break;
            case 6:
                orderAmount=checkFormatAnyPosition(codes,0,1,2,3)?mul:0;
                break;
            case 7:
                orderAmount = checkFormat(codes,0,3)?mul:0;
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                if(!checkFormatAnyPosition(codes,0,1,2,3))
                {
                    return 0;
                }
                int temp=0;
                for (String code:codes.values())
                {
                    if(code.length()>4)
                    {
                        //只有大小单双 0123
                        return 0;
                    }
                    temp=temp+code.length();
                }
                orderAmount = temp;
                break;
        }

        return orderAmount;
    }

    private static boolean checkFormat(Map<Integer, String> map, Integer... index)
    {
        for (int i = 0; i <index.length ; i++) {
            if(Strings.isEmptyOrNullAmongOf(map.get(index)))
            {
                return false;
            }
        }

        return true;
    }

    private static boolean checkFormatAnyPosition(Map<Integer, String> map, Integer... index)
    {
        for (int i = 0; i <index.length ; i++) {
            if(!Strings.isEmptyOrNullAmongOf(map.get(index)))
            {
                return true;
            }
        }

        return false;
    }

    private float getWinRate(int gameType, Lottery20Setting lottery20Setting) {
        float rate = 0.0f;
        switch (gameType) {
            case 1:
                rate = lottery20Setting.getAnythree();
                break;
            case 2:
                rate = lottery20Setting.getAnytwo();
                break;
            case 3:
                rate = lottery20Setting.getFourfix();
                break;
            case 4:
                rate = lottery20Setting.getThreefix();
                break;
            case 5:
                rate = lottery20Setting.getTwofix();
                break;
            case 6:
                rate = lottery20Setting.getOnefix();
                break;
            case 7:
                rate = lottery20Setting.getTouweifix();
                break;
            case 8:
                rate = lottery20Setting.getDxds();
                break;
        }
        return rate;
    }

    private int calTotalMoney(String string) {
        int orderTotalMoney = 0;
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String gamName = jsonObject.optString("gamName", "");
                int orderPrice = jsonObject.optInt("orderPrice", 0);

                GameIndex.QXCGameTypeCode qxcGameTypeCode = GameIndex.QXCGameTypeCode.getQXCGameTypeCode(gamName);
                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, qxcGameTypeCode.getCode());
                orderTotalMoney = orderTotalMoney + (orderPrice * orderAmount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderTotalMoney;
    }

    //{
    //    "gamName":"ry3",
    //    "orderPrice":6,
    //    "codes":[
    //        {
    //            "pos":0,
    //            "code":"453156"
    //        },
    //        {
    //            "pos":4,
    //            "code":"4342"
    //        }
    //    ]
    //}
    public Object betQXC(String betArray, String userId, String roomId, HttpServletRequest request) {
        Lottery20Setting lottery20Setting = LotteryConfigGetter.getInstance().getLottery20Setting();
        int fengTime = lottery20Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if (!lottery20Setting.getGameopen() || lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean.getNextTime().getTime() - System.currentTimeMillis() > fengTime * 1000) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (!Strings.isDigitOnly(roomId)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (Strings.isEmptyOrNullAmongOf(betArray)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }


        boolean isFormatOk = true;

        UserBean userBean = userBeanMapper.selectByUserId(userId, Integer.parseInt(roomId));
        if (userBean == null || Strings.isEmptyOrNullAmongOf(userBean.getLoginuser())) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S16);
        }

        if (userBean.getMoney().compareTo(new BigDecimal(calTotalMoney(betArray))) < 0) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S17);
        }


        //一单 可以有n注 一注可以是n元 ,大于1注就是复式
        try {
            JSONArray jsonArray = new JSONArray(betArray);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.optJSONObject(i);

                String gamName = jsonObject.optString("gamName", "");
                int orderPrice = jsonObject.optInt("orderPrice", 0);
                if (orderPrice < 1 || Strings.isEmptyOrNullAmongOf(gamName)) {
                    isFormatOk = false;
                }

                GameIndex.QXCGameTypeCode qxcGameTypeCode = GameIndex.QXCGameTypeCode.getQXCGameTypeCode(gamName);
                if (qxcGameTypeCode == null) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, qxcGameTypeCode.getCode());
                if (orderAmount < 1) {
                    isFormatOk = false;
                }

                if (orderAmount > 1 && orderPrice % orderAmount != 0) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S15);
                }

                if (!isFormatOk) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }


                BigDecimal userMoney = new BigDecimal(userBean.getMoney().toPlainString()).subtract(new BigDecimal(orderPrice * orderAmount));
                int statusDeductMoney = userBeanMapper.updateMoneyByUserId(userMoney, userId);
                if (statusDeductMoney < 1) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
                } else {
                    MarkLog markLog = new MarkLog();
                    markLog.setUserid(userId);
                    markLog.setAddtime(Calendar.getInstance().getTime());
                    markLog.setChatid("");
                    markLog.setRoomid(Integer.parseInt(roomId));
                    markLog.setGame("gamName");
                    markLog.setTuishui("");
                    markLog.setTuitime(Calendar.getInstance().getTime());
                    markLog.setType("下分");
                    markLog.setContent(GameIndex.LotteryTypeCodeList.qxc.getExplain() + qxcGameTypeCode.getExplain() + "投注");
                    markLog.setMoney(userMoney.toPlainString());

                    markLogMapper.insertSelective(markLog);
                }

                float winRate = getWinRate(qxcGameTypeCode.getCode(), lottery20Setting);
                QXCOrder qxcOrder = new QXCOrder();
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
                int status = qxcOrderMapper.insertSelective(qxcOrder);


                if (status < 1) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
                } else {
                    //websocket 发消息
                    StringBuilder sb = new StringBuilder();
                    sb.append(request.getScheme()).append("://");
                    sb.append(request.getServerName()).append(":");
                    sb.append(request.getServerPort());
                    sb.append("/sendChat");

                    List<PostParamBean> params = new ArrayList<>();
                    params.add(new PostParamBean("content", qxcOrder.getContent()));
                    params.add(new PostParamBean("userid", userId));
                    params.add(new PostParamBean("chatType", "U3"));
                    params.add(new PostParamBean("roomid", roomId));
                    params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.qxc.getGame()));
                    params.add(new PostParamBean("betTerm", lotteryOpenBean.getNextTerm()));
                    params.add(new PostParamBean("headimg", qxcOrder.getHeadimg()));
                    params.add(new PostParamBean("username", qxcOrder.getUsername()));

                    HttpRequest.getInstance().post(sb.toString(), params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isFormatOk = false;
        }

        if (!isFormatOk) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
        }


        Map<String, String> map = new HashMap<>();
        map.put("betTerm", lotteryOpenBean.getNextTerm());
        return ReturnDataBuilder.makeBaseJSON(map);
    }


    //choose 用户选中 当前彩种任选几
    private static int calculateOrderAnyChoose(int choose, int need) {
        int n = 1;
        int nm = 1;
        int m = 1;

        for (int i = 0; i < choose; i++) {
            n = n * (i + 1);
            if (choose - need - i > 0) {
                nm = nm * (choose - need - i);
            }
            if (need - i > 0) {
                m = m * (need - i);
            }
        }
        return n / nm / m;
    }

    public Object fetchQXCResult(final String roomId,final HttpServletRequest request) {

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if (lotteryOpenBean != null && lotteryOpenBean.getNextTime() != null) {
            if (System.currentTimeMillis() < lotteryOpenBean.getNextTime().getTime()) {
                return ReturnDataBuilder.error(92323, "未到开奖时间");
            }
        }
        List<PostParamBean> params = new ArrayList<>();
        params.add(new PostParamBean("lotCode", "10045"));

        HttpRequest.getInstance().get(qxcUrl, params, new HttpCallBack() {
            @Override
            public void onError(Exception ex) {

            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.optInt("errorCode", -1) == 0) {
                        JSONObject jsonData = jsonObject.optJSONObject("result").optJSONObject("data");
                        //{"issue":"23142","drawResult":"3,3,8,6,1,5+3","drawTime":"2023-12-10 21:31:38","code":"qxc"}
                        String term = jsonData.optString("preDrawIssue", "");
                        String result = jsonData.optString("preDrawCode", "");
                        String time = jsonData.optString("preDrawTime", "");

                        String[] resultSplit =result.split(",");
                        StringBuilder sb=new StringBuilder();
                        for (int i = 0; i < resultSplit.length-1; i++) {
                            sb.append(resultSplit[i]);
                        }
                        sb.append(",").append(resultSplit[resultSplit.length-1]);
                        result=sb.toString();

                        if (!Strings.isEmptyOrNullAmongOf(term, result, time)) {
                            result = result.replaceAll(",", "").replaceAll("\\+", "");
                            Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            calQXCOrder(request,roomId,term, result, date, lotteryOpenBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return ReturnDataBuilder.makeBaseJSON(null);
    }

    private void calQXCOrder(HttpServletRequest request,String roomId,String term, String codes, Date time, LotteryOpenBean lotteryOpenBean) {
        boolean isShouldAdd = false;

        if (lotteryOpenBean == null) {
            isShouldAdd = true;
        } else {
            if (!Strings.isEmptyOrNullAmongOf(lotteryOpenBean.getTerm()) && Integer.parseInt(term) > Integer.parseInt(lotteryOpenBean.getTerm())) {
                isShouldAdd = true;
            }
        }

        if (!isShouldAdd) {
            return;
        }

        LotteryOpenBean newTermBean = new LotteryOpenBean();
        newTermBean.setCode(codes);
        newTermBean.setType(GameIndex.LotteryTypeCodeList.qxc.getCode());
        newTermBean.setNextTerm(String.valueOf(Integer.parseInt(term) + 1));
        newTermBean.setTerm(term);
        newTermBean.setTime(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(isFirstSunday){
            weekDay = weekDay - 1;
            if(weekDay == 0){
                weekDay = 7;
            }
        }

        //周2 5 7开奖
        switch (weekDay) {
            case 2:
                calendar.setTime(new Date(time.getTime() + (3 * 24 * 3600 * 1000)));
                break;
            case 5:
            case 7:
                calendar.setTime(new Date(time.getTime() + (2 * 24 * 3600 * 1000)));
                break;
            default:
                //不是的话就假的开奖
                return;
        }
        newTermBean.setNextTime(calendar.getTime());
        newTermBean.setRoomid(roomId);
        newTermBean.setCode(codes);
        MyLog.e(new Gson().toJson(newTermBean));
        lotteryOpenBeanMapper.insertSelective(newTermBean);

        //插入开奖号码后开始结算
        List<QXCOrder> qxcOrderList = qxcOrderMapper.selectOrderByStatus(0, term);
        if (qxcOrderList == null || qxcOrderList.isEmpty()) {
            return;
        }

        Map<String,Float> map=new HashMap<>();
        for (QXCOrder qxcOrder : qxcOrderList) {

            int winTimes=0;

            Map<Integer,String> playerBetCodesBeans=new HashMap<>();
            try {
                JSONArray jsonArray=new JSONArray(qxcOrder.getContent());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.optJSONObject(i);
                    playerBetCodesBeans.put(jsonObject.optInt("pos",-1),jsonObject.optString("code","").trim().replaceAll(" ",""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //结算算法
            switch (qxcOrder.getGameType())
            {
                case 1:
                    if(playerBetCodesBeans.size()>0 && playerBetCodesBeans.get(0)!=null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0)))
                    {
                        winTimes=AnyChooseCalWin.getInstance().getWinTimes(lotteryOpenBean.getCode(),playerBetCodesBeans.get(0),3);
                        sumBeforeWin(map,qxcOrder,winTimes);
                    }
                    break;
                case 2:
                    if(playerBetCodesBeans.size()>0 && playerBetCodesBeans.get(0)!=null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0)))
                    {
                        winTimes=AnyChooseCalWin.getInstance().getWinTimes(lotteryOpenBean.getCode(),qxcOrder.getContent(),4);
                        sumBeforeWin(map,qxcOrder,winTimes);
                    }
                    break;
                case 3:
                    if(FixChooseCalWin.getInstance().calFixIsWin(lotteryOpenBean.getCode(),playerBetCodesBeans,0,1,2,3))
                    {
                        sumBeforeWin(map,qxcOrder,1);
                    }
                    break;
                case 4:
                    if(FixChooseCalWin.getInstance().calFixIsWin(lotteryOpenBean.getCode(),playerBetCodesBeans,0,1,2))
                    {
                        sumBeforeWin(map,qxcOrder,1);
                    }
                    break;
                case 5:
                    if(FixChooseCalWin.getInstance().calFixIsWin(lotteryOpenBean.getCode(),playerBetCodesBeans,0,1))
                    {
                        sumBeforeWin(map,qxcOrder,1);
                    }
                    break;
                case 6:
                    winTimes=FixChooseCalWin.getInstance().calFixOneIsWin(lotteryOpenBean.getCode(),playerBetCodesBeans,0,1,2,3);
                    if(winTimes>0)
                    {
                        sumBeforeWin(map,qxcOrder,winTimes);
                    }
                    break;
                case 7:
                    if(FixChooseCalWin.getInstance().calFixIsWin(lotteryOpenBean.getCode(),playerBetCodesBeans,0,3))
                    {
                        sumBeforeWin(map,qxcOrder,1);
                    }
                    break;
                case 8:
                    winTimes=FixChooseCalWin.getInstance().calDXDS(lotteryOpenBean.getCode(),playerBetCodesBeans,0,1,2,3);
                    if(winTimes>0)
                    {
                        sumBeforeWin(map,qxcOrder,winTimes);
                    }
                    break;
            }

            //结算钱给客户
            win(map,term,roomId,request);
        }

    }

    private void sumBeforeWin(Map<String,Float> map,QXCOrder qxcOrder,int winTimes)
    {
        if(winTimes<1)
        {
            return;
        }
        Float beforeWin=map.get(qxcOrder.getUserid());
        float totalMoney = qxcOrder.getUnitprice()*winTimes*qxcOrder.getWinrate();
        if(beforeWin!=null)
        {
            map.put(qxcOrder.getUserid(),beforeWin+totalMoney);
        }
        else
        {
            map.put(qxcOrder.getUserid(),totalMoney);
        }
    }


    private void win(Map<String,Float> map,String term,String roomId,HttpServletRequest request)
    {
        //应该用事务的 但是数据库面目全非 算了
        for (Map.Entry<String, Float> entry : map.entrySet())
        {
            userBeanMapper.addUserMoney(String.valueOf(entry.getValue()),entry.getKey());
        }

        //发开奖公告
        List<PostParamBean> params = new ArrayList<>();
        params.add(new PostParamBean("content", "第 $kjqh 期&nbsp;开&nbsp;奖&nbsp;号&nbsp;码<br><br>$haomachuan<br><br>第 $term 期已开启下注!"));
        params.add(new PostParamBean("userid", "system"));
        params.add(new PostParamBean("chatType", "U3"));
        params.add(new PostParamBean("roomid", roomId));
        params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.qxc.getGame()));
        params.add(new PostParamBean("imgType", "robot"));
        params.add(new PostParamBean("username", "播报员"));
        params.add(new PostParamBean("betTerm",term));

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":");
        sb.append(request.getServerPort());
        sb.append("/sendChat");

        HttpRequest.getInstance().post(sb.toString(), params);
    }

}
