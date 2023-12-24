package com.example.tt.Service;

import com.example.tt.Bean.*;
import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.OpenResult.FixChooseCalWin;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.*;
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

    @Autowired(required = false)
    PreSetResultMapper preSetResultMapper;

    static SessionStorage sessionStorage = SessionStorage.getInstance();
//    final String qxcUrl="https://api.api68.com/QuanGuoCai/getLotteryInfo.do?lotCode=10045";//七星彩开奖地址

    final String qxcUrl = "http://localhost:8653/fakeOpenResult?lotteryName=qxc";//假七星彩开奖地址

    private static int check(JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        Map<Integer, String[]> codes = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String str = jsonObject.optString("code", "").trim().replaceAll(" ", "");
            String[] nums = str.split(",");
            codes.put(jsonObject.optInt("pos", -1), nums);
            if (nums.length == 0 || !Strings.isDigitOnly(nums)) {
                return 0;
            } else {
                mul = mul * nums.length;
            }
        }

        if (codes.isEmpty()) {
            return 0;
        }

        switch (type) {
            case 1:
                orderAmount = calculateOrderAnyChoose(codes.get(0).length, 3);
                break;
            case 2:
                orderAmount = calculateOrderAnyChoose(codes.get(0).length, 2);
                break;
            case 3:
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2, 3) ? mul : 0;
                break;
            case 4:
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2) ? mul : 0;
                break;
            case 5:
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1) ? mul : 0;
                break;
            case 6:
                orderAmount = AnyChooseCalWin.checkFormatAnyPosition(codes, 0, 1, 2, 3) ? mul : 0;
                break;
            case 7:
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 3) ? mul : 0;
                break;
            case 8:
                if (!AnyChooseCalWin.checkFormatAnyPosition(codes, 0, 1, 2, 3)) {
                    return 0;
                }
                int temp = 0;
                for (String code[] : codes.values()) {
                    if (code.length > 4) {
                        //只有大小单双 0123
                        return 0;
                    }
                    temp = temp + code.length;
                }
                orderAmount = temp;
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
                if (jsonObject == null) {
                    return -1;
                }
                String gameName = jsonObject.optString("gameName", "");
                int unitPrice = jsonObject.optInt("unitPrice", 0);

                GameIndex.QXCGameTypeCode qxcGameTypeCode = GameIndex.QXCGameTypeCode.getQXCGameTypeCode(gameName);
                if (qxcGameTypeCode == null) {
                    return -1;
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, qxcGameTypeCode.getCode());
                orderTotalMoney = orderTotalMoney + (unitPrice * orderAmount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderTotalMoney;
    }

    //{
    //    "gameName":"ry3",
    //    "orderPrice":6,
    //    "codes":[
    //        {
    //            "pos":0,
    //            "code":"4,5,3,1,5,6"
    //        },
    //        {
    //            "pos":4,
    //            "code":"4,3,4,2"
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

        if (lotteryOpenBean.getNextTime().getTime() - System.currentTimeMillis() < fengTime * 1000L) {
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

        int calTotalMoney = calTotalMoney(betArray);
        MyLog.e("totalMoney-->" + calTotalMoney);
        if (calTotalMoney <= 0 || userBean.getMoney().compareTo(new BigDecimal(calTotalMoney)) < 0) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S17);
        }

        //一单 可以有n注 一注可以是n元 ,大于1注就是复式
        try {
            JSONArray jsonArray = new JSONArray(betArray);
            if (jsonArray.length() < 1) {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S19);
            }
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.optJSONObject(i);

                String gameName = jsonObject.optString("gameName", "");
                int unitPrice = jsonObject.optInt("unitPrice", 0);
                int totalMoney = jsonObject.optInt("money", 0);
                if (unitPrice < 1 || Strings.isEmptyOrNullAmongOf(gameName)) {
                    isFormatOk = false;
                }

                GameIndex.QXCGameTypeCode qxcGameTypeCode = GameIndex.QXCGameTypeCode.getQXCGameTypeCode(gameName);
                if (qxcGameTypeCode == null) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                if (unitPrice < lottery20Setting.getMinbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery20Setting.getMinbet()));
                }

                if (unitPrice > lottery20Setting.getMaxbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery20Setting.getMinbet()));
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, qxcGameTypeCode.getCode());
                StringBuilder chatContent = new StringBuilder();
                chatContent.append(qxcGameTypeCode.getExplain()).append(":");
                for (int j = 0; j < codes.length(); j++) {
                    JSONObject temp = codes.optJSONObject(j);
                    chatContent.append(temp.optString("code")).append("|");
                }
                chatContent.deleteCharAt(chatContent.length() - 1);
                if (orderAmount < 1) {
                    isFormatOk = false;
                }

                if (orderAmount > 1 && totalMoney % orderAmount != 0) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S15);
                }

                if (!isFormatOk) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }


                float winRate = getWinRate(qxcGameTypeCode.getCode(), lottery20Setting);
                QXCOrder qxcOrder = new QXCOrder();
                qxcOrder.setAddtime(Calendar.getInstance().getTime());
                qxcOrder.setContent(jsonObject.toString());
                qxcOrder.setTerm(lotteryOpenBean.getNextTerm());
                qxcOrder.setMoney(unitPrice * orderAmount);
                qxcOrder.setStatus(GameIndex.OrderCalculateStatus.unCalculate.getCode());
                qxcOrder.setGamename(GameIndex.LotteryTypeCodeList.qxc.getGame());
                qxcOrder.setGameType(GameIndex.LotteryTypeCodeList.qxc.getCode());
                qxcOrder.setOrderamount(orderAmount);
                qxcOrder.setUnitprice(unitPrice);
                qxcOrder.setWinrate(winRate);
                qxcOrder.setUsername(userBean.getUsername());
                qxcOrder.setHeadimg(userBean.getHeadimg());
                qxcOrder.setMingci("");
                qxcOrder.setJia(userBean.getJia());
                qxcOrder.setUserid(userId);
                qxcOrder.setRoomid(Integer.parseInt(roomId));
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
                    params.add(new PostParamBean("content", chatContent.toString()));
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

            //计算总共的钱
            BigDecimal userMoney = new BigDecimal(userBean.getMoney().toPlainString()).subtract(new BigDecimal(calTotalMoney));
            int statusDeductMoney = userBeanMapper.updateMoneyByUserId(userMoney, userId);
            if (statusDeductMoney < 1) {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
            } else {
                MarkLog markLog = new MarkLog();
                markLog.setUserid(userId);
                markLog.setAddtime(Calendar.getInstance().getTime());
                markLog.setChatid("");
                markLog.setRoomid(Integer.parseInt(roomId));
                markLog.setGame(GameIndex.LotteryTypeCodeList.pk10.getGame());
                markLog.setTuishui("");
                markLog.setTuitime(Calendar.getInstance().getTime());
                markLog.setType("下分");
                markLog.setContent(GameIndex.LotteryTypeCodeList.qxc.getExplain() + "投注");
                markLog.setMoney(userMoney.toPlainString());

                markLogMapper.insertSelective(markLog);
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


    //choose 用户选中（任选x） 当前有几注
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

    public Object fetchQXCResult(final String roomId, final String baseUrl) {

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        if (lotteryOpenBean != null && lotteryOpenBean.getNextTime() != null) {
            if (System.currentTimeMillis() < lotteryOpenBean.getNextTime().getTime()) {
                return ReturnDataBuilder.error(92323, "未到开奖时间");
            }
        }

        HttpRequest.getInstance().get(qxcUrl, null, new HttpCallBack() {
            @Override
            public void onError(Exception ex) {
                MyLog.e("七星彩开奖结果请求失败");
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
                        String nextTermTime = jsonData.optString("drawTime", "");

                        String[] resultSplit = result.split(",");
                        if (resultSplit.length < 4) {
                            MyLog.e("七星彩开奖结果错误");
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 4; i++) {
                            sb.append(resultSplit[i]);
                        }
//                        sb.append(",").append(resultSplit[resultSplit.length-1]);
                        result = sb.toString();

                        if (!Strings.isEmptyOrNullAmongOf(term, result, time)) {
                            result = result.replaceAll(",", "").replaceAll("\\+", "");
                            Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            Date date2 = TimeUtils.string2Date(nextTermTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                            String type = GameIndex.LotteryTypeCodeList.qxc.getCode() + "";
                            PreSetResult preSetResult = preSetResultMapper.selectByTermAndType(type, term);
                            if (preSetResult != null && !Strings.isEmptyOrNullAmongOf(preSetResult.getCode())) {
                                result = preSetResult.getCode().replaceAll(",", "").replaceAll("\\|", "");
                            }
                            calQXCOrder(baseUrl, roomId, term, result, date, date2, lotteryOpenBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return ReturnDataBuilder.makeBaseJSON(null);
    }

    private void calQXCOrder(String baseUrl, String roomId, String term, String codes, Date time, Date nextTermTime, LotteryOpenBean lotteryOpenBean) {
        boolean isShouldAdd = false;

        if (lotteryOpenBean == null) {
            isShouldAdd = true;
        } else {
            if (!Strings.isEmptyOrNullAmongOf(lotteryOpenBean.getTerm()) && Integer.parseInt(term) > Integer.parseInt(lotteryOpenBean.getTerm())) {
                isShouldAdd = true;
            }
        }

        MyLog.e("七星彩采集--->" + isShouldAdd + " " + term);
        if (!isShouldAdd) {
            return;
        }

        LotteryOpenBean newTermBean = new LotteryOpenBean();
        newTermBean.setCode(codes);
        newTermBean.setType(GameIndex.LotteryTypeCodeList.qxc.getCode());
        newTermBean.setNextTerm(String.valueOf(Integer.parseInt(term) + 1));
        newTermBean.setTerm(term);
        newTermBean.setTime(time);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(time);
//
//        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
//        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//        if (isFirstSunday) {
//            weekDay = weekDay - 1;
//            if (weekDay == 0) {
//                weekDay = 7;
//            }
//        }
//
//        //周2 5 7开奖
//        switch (weekDay) {
//            case 2:
//                calendar.setTime(new Date(time.getTime() + (3 * 24 * 3600 * 1000)));
//                break;
//            case 5:
//            case 7:
//                calendar.setTime(new Date(time.getTime() + (2 * 24 * 3600 * 1000)));
//                break;
//            default:
//                //不是的话就假的开奖
//                return;
//        }

        //暂时不用 开奖错误了再用
//        newTermBean.setNextTime(calendar.getTime());
        newTermBean.setNextTime(nextTermTime);
        newTermBean.setRoomid(roomId);
        newTermBean.setCode(codes);
        MyLog.e(new Gson().toJson(newTermBean));
        lotteryOpenBeanMapper.insertSelective(newTermBean);

        //发开奖公告
        sendOpenLotteryResult(codes, term, roomId, baseUrl);

        calWinMoneyForPlayer();
    }

    //结算钱给客户
    private void calWinMoneyForPlayer() {
        //插入开奖号码后开始结算
        List<QXCOrder> qxcOrderList = qxcOrderMapper.selectOrderByStatus(0);
        if (qxcOrderList == null || qxcOrderList.isEmpty()) {
            return;
        }


        Map<String, Float> map = new HashMap<>();
        List<QXCOrder> qxcOrderListCal = new ArrayList<>();
        for (QXCOrder qxcOrder : qxcOrderList) {

            String openResultCodes = LotteryConfigGetter.getInstance().getOpenResultCodes(GameIndex.LotteryTypeCodeList.qxc.getCode(), qxcOrder.getTerm());
            if (Strings.isEmptyOrNullAmongOf(openResultCodes)) {
                MyLog.e("结算失败，获取开奖号码是空");
                continue;
            }

            int winTimes = 0;

            int gameType = -1;
            Map<Integer, String[]> playerBetCodesBeans = new HashMap<>();
            try {
                JSONObject data = new JSONObject(qxcOrder.getContent());
                GameIndex.QXCGameTypeCode typeCode = GameIndex.QXCGameTypeCode.getQXCGameTypeCode(data.optString("gameName", ""));
                if (typeCode != null) {
                    gameType = typeCode.getCode();
                }
                JSONArray jsonArray = data.optJSONArray("codes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    String codes = jsonObject.optString("code", "").trim().replaceAll(" ", "");
                    playerBetCodesBeans.put(jsonObject.optInt("pos", -1), codes.split(","));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //结算算法
            switch (gameType) {
                case 1:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 3);
                    }
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 2:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 2);
                    }
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 3:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 4:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 5:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 6:
                    winTimes = FixChooseCalWin.getInstance().calFixOneIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 7:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 3);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
                case 8:
                    winTimes = FixChooseCalWin.getInstance().calDXDS(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3);
                    sumBeforeWin(map, qxcOrder, winTimes);
                    break;
            }

            qxcOrderListCal.add(qxcOrder);

        }

        //结算钱给客户
        winOrLost(map, qxcOrderListCal);
    }

    private void sumBeforeWin(Map<String, Float> map, QXCOrder qxcOrder, int winTimes) {
        if (winTimes < 1) {
            qxcOrder.setStatus(GameIndex.OrderCalculateStatus.lost.getCode());
            return;
        } else {
            qxcOrder.setStatus(GameIndex.OrderCalculateStatus.win.getCode());
        }
        Float beforeWin = map.get(qxcOrder.getUserid());
        float totalMoney = qxcOrder.getUnitprice() * winTimes * qxcOrder.getWinrate();
        if (beforeWin != null) {
            map.put(qxcOrder.getUserid(), beforeWin + totalMoney);
        } else {
            map.put(qxcOrder.getUserid(), totalMoney);
        }
    }


    private void winOrLost(Map<String, Float> map, List<QXCOrder> qxcOrderListCal) {
        //应该用事务的 但是数据库面目全非 算了
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            userBeanMapper.addUserMoney(BigDecimal.valueOf(entry.getValue()), entry.getKey());
        }

        //0 未结算 1赢了 2输了 9撤单
        for (int i = 0; i < qxcOrderListCal.size(); i++) {
            QXCOrder qxcOrder = new QXCOrder();
            qxcOrder.setId(qxcOrderListCal.get(i).getId());
            qxcOrder.setStatus(qxcOrderListCal.get(i).getStatus());
            qxcOrderMapper.updateByPrimaryKeySelective(qxcOrder);
        }

    }

    private void sendOpenLotteryResult(String result, String term, String roomId, String baseUrl) {
        //发开奖公告
        List<PostParamBean> params = new ArrayList<>();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("第 ").append(term).append(" 期&nbsp;开&nbsp;奖&nbsp;号&nbsp;码 <br><br>");
        for (int i = 0; i < result.length(); i++) {
            sbContent.append("<span class='pk_");
            sbContent.append((result.charAt(i) - '0'));
            sbContent.append("'>").append(result.charAt(i) - '0').append("</span>");
        }
        sbContent.append("<br><br>第 ").append(Integer.parseInt(term) + 1).append(" 期已开启下注!");

        params.add(new PostParamBean("content", sbContent.toString()));
        params.add(new PostParamBean("userid", "system"));
        params.add(new PostParamBean("chatType", "S3"));
        params.add(new PostParamBean("roomid", roomId));
        params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.qxc.getGame()));
        params.add(new PostParamBean("imgType", "robot"));
        params.add(new PostParamBean("username", "播报员"));
        params.add(new PostParamBean("betTerm", String.valueOf(Integer.parseInt(term) + 1)));

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append("/sendChat");

        HttpRequest.getInstance().post(sb.toString(), params);
    }

    public Object getRemainTimeAndUser(String userId, String roomId, HttpServletRequest request) {
        Lottery20Setting lottery20Setting = LotteryConfigGetter.getInstance().getLottery20Setting();
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
        BigDecimal money = userBeanMapper.selectMoneyByUserId(userId, Integer.parseInt(roomId));

        if (!lottery20Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery20Setting.getFengtime();
        if (reamain < 0) {
            reamain = 0;
        }
        String remainTime = String.valueOf(reamain);
        Map<String, String> map = new HashMap<>();
        map.put("remainTime", remainTime);
        map.put("money", Strings.cutOff(money, 2));
        map.put("term", lotteryOpenBean.getNextTerm());

        return ReturnDataBuilder.makeBaseJSON(map);
    }

    public Object getRemainTime(String userId, String roomId, HttpServletRequest request) {
        Lottery20Setting lottery20Setting = LotteryConfigGetter.getInstance().getLottery20Setting();
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());

        if (!lottery20Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery20Setting.getFengtime();
        if (reamain < 0) {
            reamain = 0;
        }
        String remainTime = String.valueOf(reamain);
        Map<String, String> map = new HashMap<>();
        map.put("remainTime", remainTime);
        map.put("codes",lotteryOpenBean.getCode());
        map.put("openTime", String.valueOf(lotteryOpenBean.getNextTime().getTime()));
        map.put("term", lotteryOpenBean.getTerm());

        return ReturnDataBuilder.makeBaseJSON(map);
    }

    public String cancelOrder(String id) {
        if (!Strings.isDigitOnly(id)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        QXCOrder qxcOrder = new QXCOrder();
        qxcOrder.setId(Integer.parseInt(id));
        qxcOrder.setStatus(GameIndex.OrderCalculateStatus.quit.getCode());
        int status = qxcOrderMapper.updateByPrimaryKeySelective(qxcOrder);

        return ReturnDataBuilder.returnData(status > 0);
    }
}
