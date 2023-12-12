package com.example.tt.Service;

import com.example.tt.Bean.*;
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

    public static int check(JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String str = jsonArray.optString(i).trim().replaceAll(" ", "");
            if (str.length() < 1 || !Strings.isDigitOnly(str)) {
                return 0;
            } else {
                stringList.add(str);
                mul = mul * str.length();
            }
        }

        switch (type) {
            case 1:
                orderAmount = calculateOrderAnyChoose(stringList.get(0).length(), 3);
                break;
            case 2:
                orderAmount = calculateOrderAnyChoose(stringList.get(0).length(), 2);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                orderAmount = mul;
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                orderAmount = 1;
                break;

        }

        return orderAmount;
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
                rate = lottery20Setting.getDa();
                break;
            case 9:
                rate = lottery20Setting.getXiao();
                break;
            case 10:
                rate = lottery20Setting.getDan();
                break;
            case 11:
                rate = lottery20Setting.getShuang();
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

    public Object fetchQXCResult() {

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if (lotteryOpenBean != null && lotteryOpenBean.getNextTime() != null) {
            if (System.currentTimeMillis() < lotteryOpenBean.getNextTime().getTime()) {
                return ReturnDataBuilder.error(92323, "未到开奖时间");
            }
        }
        List<PostParamBean> params = new ArrayList<>();
        params.add(new PostParamBean("code", "qxc"));
        params.add(new PostParamBean("format", "json"));
        params.add(new PostParamBean("rows", "1"));
        HttpRequest.getInstance().get("https://api.api68.com/QuanGuoCai/getLotteryInfo.do?lotCode=10045", params, new HttpCallBack() {
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
                        if (!Strings.isEmptyOrNullAmongOf(term, result, time)) {
                            result = result.replaceAll(",", "").replaceAll("\\+", "");
                            Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            calQXCOrder(term, result, date, lotteryOpenBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return ReturnDataBuilder.makeBaseJSON(null);
    }

    private void calQXCOrder(String term, String codes, Date time, LotteryOpenBean lotteryOpenBean) {
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
        //周2 5 7开奖
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                calendar.setTimeInMillis(time.getTime() + (3 * 24 * 3600 * 1000));
                break;
            case 5:
            case 7:
                calendar.setTimeInMillis(time.getTime() + (2 * 24 * 3600 * 1000));
                break;
        }
        newTermBean.setNextTime(calendar.getTime());
        newTermBean.setRoomid(lotteryOpenBean.getRoomid());
        newTermBean.setCode(codes);
        lotteryOpenBeanMapper.insertSelective(newTermBean);

        //插入开奖号码后开始结算
        List<QXCOrder> qxcOrderList = qxcOrderMapper.selectOrderByStatus(0, term);
        if (qxcOrderList == null || qxcOrderList.size() < 1) {
            return;
        }

        Map<String,Float> map=new HashMap<>();
        for (QXCOrder qxcOrder : qxcOrderList) {

            switch (qxcOrder.getGameType())
            {
                case 1:
                    if(isWindAnyChoose(3,codes,qxcOrder))
                    {
                        //任选3 3 就是中奖
                        sumBeforeWin(map,qxcOrder);
                    }
                    break;
                case 2:
                    if(isWindAnyChoose(4,codes,qxcOrder))
                    {
                        //任选4 4 就是中奖
                        sumBeforeWin(map,qxcOrder);
                    }
                    break;
                case 3:

                    break;
            }
        }


    }

    private boolean isWindAnyChoose(int times,String codes,QXCOrder qxcOrder)
    {
        int countEq=0;
        for (int i = 0; i < codes.length(); i++) {
            char cha=codes.charAt(i);
            if(countEq>=times)
            {
                break;
            }
            for (int j = 0; j < qxcOrder.getContent().length(); j++) {
                if(cha==qxcOrder.getContent().charAt(j))
                {
                    countEq++;
                    if(countEq>=times)
                    {
                        break;
                    }
                }
            }
        }

        return countEq>=times;
    }

    private void sumBeforeWin(Map<String,Float> map,QXCOrder qxcOrder)
    {
        Float beforeWin=map.get(qxcOrder.getUserid());
        float totalMoney = qxcOrder.getWinrate() * qxcOrder.getMoney();
        if(beforeWin!=null)
        {
            map.put(qxcOrder.getUserid(),beforeWin+totalMoney);
        }
        else
        {
            map.put(qxcOrder.getUserid(),totalMoney);
        }
    }


    private void win(QXCOrder qxcOrder)
    {
        float totalMoney = qxcOrder.getWinrate() * qxcOrder.getMoney();
        userBeanMapper.addUserMoney(String.valueOf(totalMoney),qxcOrder.getUserid());
    }

}
