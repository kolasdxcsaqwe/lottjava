package com.example.tt.Service;

import com.example.tt.Bean.*;
import com.example.tt.OpenResult.*;
import com.example.tt.dao.*;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FC3DService {

    @Autowired(required = false)
    FC3DOrderMapper fc3DOrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    @Autowired(required = false)
    MarkLogMapper markLogMapper;

    @Autowired(required = false)
    PreSetResultMapper preSetResultMapper;

    public final static int[] zu3 = {1, 2, 3, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5, 5, 4, 5, 4, 3, 3, 3, 1, 2, 1};
    public final static int[] zu6 = {1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9, 8, 7, 5, 4, 3, 2, 1, 1};


//    final String fc3dUrl="https://api.api68.com/QuanGuoCai/getLotteryInfo1.do?lotCode=10041";//福彩3D开奖地址

    final String fc3dUrl = "http://localhost:8653/fakeOpenResult?lotteryName=fc3d";//假福彩3d开奖地址


    private static int check(JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        int sum = 0;
        Map<Integer, String[]> codes = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String str = jsonObject.optString("code", "").trim().replaceAll(" ", "");
            String[] nums = str.split(",");
            codes.put(jsonObject.optInt("pos", -1), nums);
            if (nums.length == 0) {
                return 0;
            } else {
                mul = mul * nums.length;
                sum = sum + nums.length;
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
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2) ? mul : 0;
                break;
            case 4:
                orderAmount = mul > 1 ? mul * (mul - 1) : 0;
                break;
            case 5:
                orderAmount = mul > 2 ? mul * (mul - 1) * (mul - 2) / 6 : 0;
                break;
            case 6:
                if (codes.get(0).length > 1) {
                    for (int i = 0; i < codes.get(0).length; i++) {
                        if (Strings.isDigitOnly(codes.get(0)[i])) {
                            int num = Integer.parseInt(codes.get(0)[i]);
                            if (num > 0 && num < 27) {
                                orderAmount = orderAmount + zu3[num - 1];
                            }
                        }
                    }
                }
                break;
            case 7:
                if (codes.get(0).length > 1) {
                    for (int i = 0; i < codes.get(0).length; i++) {
                        if (Strings.isDigitOnly(codes.get(0)[i])) {
                            int num = Integer.parseInt(codes.get(0)[i]);
                            if (num > 2 && num < 25) {
                                orderAmount = orderAmount + zu6[num - 1];
                            }
                        }
                    }
                }
                break;
            case 8:
            case 9:
                orderAmount = mul;
                break;
            case 10:
                orderAmount = sum;
                break;
            case 11:
                if (!AnyChooseCalWin.checkFormatAnyPosition(codes, 0, 1, 2)) {
                    return 0;
                }
                for (String[] code : codes.values()) {
                    if (code.length > 4) {
                        //只有大小单双 0123
                        return 0;
                    }
                }
                orderAmount = sum;
                break;
        }

        return orderAmount;
    }

    public String cancelOrder(String id) {
        if (!Strings.isDigitOnly(id)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        FC3DOrder fc3DOrder = new FC3DOrder();
        fc3DOrder.setId(Integer.parseInt(id));
        fc3DOrder.setStatus(GameIndex.OrderCalculateStatus.quit.getCode());
        int status = fc3DOrderMapper.updateByPrimaryKeySelective(fc3DOrder);

        return ReturnDataBuilder.returnData(status > 0);
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

    private float getWinRate(int gameType, Lottery21Setting lottery21Setting) {
        float rate = 0.0f;
        switch (gameType) {
            case 1:
                rate = lottery21Setting.getAnytwo();
                break;
            case 2:
                rate = lottery21Setting.getAnyone();
                break;
            case 3:
                rate = lottery21Setting.getThreefix();
                break;
            case 4:
                rate = lottery21Setting.getCombinethree();
                break;
            case 5:
                rate = lottery21Setting.getCombinesix();
                break;
            case 6:
                rate = lottery21Setting.getCombinethreesum();
                break;
            case 7:
                rate = lottery21Setting.getCombinesixsum();
                break;
            case 8:
                rate = lottery21Setting.getFronttwofix();
                break;
            case 9:
                rate = lottery21Setting.getBacktwofix();
                break;
            case 10:
                rate = lottery21Setting.getOnefix();
                break;
            case 11:
                rate = lottery21Setting.getDxds();
                break;
        }
        return rate;
    }

    public Object betFC3D(String betArray, String userId, String roomId, HttpServletRequest request) {
        Lottery21Setting lottery21Setting = LotteryConfigGetter.getInstance().getLottery21Setting();
        int fengTime = lottery21Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.fc3d.getCode());
        if (!lottery21Setting.getGameopen() || lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
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

                GameIndex.FC3DGameTypeCode fc3DGameTypeCode = GameIndex.FC3DGameTypeCode.getFC3DGameTypeCode(gameName);
                if (fc3DGameTypeCode == null) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                if (unitPrice < lottery21Setting.getMinbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery21Setting.getMinbet()));
                }

                if (unitPrice > lottery21Setting.getMaxbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery21Setting.getMinbet()));
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, fc3DGameTypeCode.getCode());
                StringBuilder chatContent = new StringBuilder();
                chatContent.append("<span style='color:#fe6c00';font-size:3rem>")
                        .append(fc3DGameTypeCode.getExplain()).append("</span><br>");
                for (int j = 0; j < codes.length(); j++) {
                    JSONObject temp = codes.optJSONObject(j);
                    if(fc3DGameTypeCode.getCode()==GameIndex.FC3DGameTypeCode.dxds.getCode())
                    {
                        String[] codesArray=temp.optString("code","").split(",");
                        if(codesArray.length>0)
                        {
                            for(String str:codesArray)
                            {
                                int num=str.charAt(0)-'0';
                                if(GameIndex.DXDS.length>num)
                                {
                                    chatContent.append(GameIndex.DXDS[num]);
                                }
                            }
                            chatContent.append("<br>");
                        }
                    }
                    else
                    {
                        chatContent.append(temp.optString("code","")).append("<br>");
                    }
                }
                if (orderAmount < 1) {
                    isFormatOk = false;
                }

                if (orderAmount > 1 && totalMoney % orderAmount != 0) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S15);
                }

                if (!isFormatOk) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }


                float winRate = getWinRate(fc3DGameTypeCode.getCode(), lottery21Setting);
                FC3DOrder fc3DOrder = new FC3DOrder();
                fc3DOrder.setAddtime(Calendar.getInstance().getTime());
                fc3DOrder.setContent(jsonObject.toString());
                fc3DOrder.setTerm(lotteryOpenBean.getNextTerm());
                fc3DOrder.setMoney(unitPrice * orderAmount);
                fc3DOrder.setStatus(GameIndex.OrderCalculateStatus.unCalculate.getCode());
                fc3DOrder.setGamename(GameIndex.LotteryTypeCodeList.fc3d.getGame());
                fc3DOrder.setGametype(GameIndex.LotteryTypeCodeList.fc3d.getCode());
                fc3DOrder.setOrderamount(orderAmount);
                fc3DOrder.setUnitprice(unitPrice);
                fc3DOrder.setWinrate(winRate);
                fc3DOrder.setUsername(userBean.getUsername());
                fc3DOrder.setHeadimg(userBean.getHeadimg());
                fc3DOrder.setMingci("");
                fc3DOrder.setJia(userBean.getJia());
                fc3DOrder.setUserid(userId);
                fc3DOrder.setRoomid(Integer.parseInt(roomId));
                int status = fc3DOrderMapper.insertSelective(fc3DOrder);


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
                    params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.fc3d.getGame()));
                    params.add(new PostParamBean("betTerm", lotteryOpenBean.getNextTerm()));
                    params.add(new PostParamBean("headimg", fc3DOrder.getHeadimg()));
                    params.add(new PostParamBean("username", fc3DOrder.getUsername()));

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
                markLog.setContent(GameIndex.LotteryTypeCodeList.fc3d.getExplain() + "投注");
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

                GameIndex.FC3DGameTypeCode fc3DGameTypeCode = GameIndex.FC3DGameTypeCode.getFC3DGameTypeCode(gameName);
                if (fc3DGameTypeCode == null) {
                    return -1;
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(codes, fc3DGameTypeCode.getCode());
                orderTotalMoney = orderTotalMoney + (unitPrice * orderAmount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderTotalMoney;
    }

    public Object fetchFC3DResult(final String roomId, final String baseUrl) {
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.fc3d.getCode());
        if (lotteryOpenBean != null && lotteryOpenBean.getNextTime() != null) {
            if (System.currentTimeMillis() < lotteryOpenBean.getNextTime().getTime()) {
                return ReturnDataBuilder.error(92323, "未到开奖时间");
            }
        }

        HttpRequest.getInstance().get(fc3dUrl, null, new HttpCallBack() {
            @Override
            public void onError(Exception ex) {
                MyLog.e("福彩3D开奖结果请求失败");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.optInt("errorCode", -1) == 0) {
                        JSONObject jsonData = jsonObject.optJSONObject("result").optJSONObject("data");
                        String term = jsonData.optString("preDrawIssue", "");
                        String result = jsonData.optString("preDrawCode", "");
                        String time = jsonData.optString("preDrawTime", "");
                        String nextTermTime = jsonData.optString("drawTime", "");

                        String[] resultSplit = result.split(",");
                        if (resultSplit.length < 3) {
                            MyLog.e("福彩3d开奖结果错误");
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 3; i++) {
                            sb.append(resultSplit[i]);
                        }
//                        sb.append(",").append(resultSplit[resultSplit.length-1]);
                        result = sb.toString();

                        if (!Strings.isEmptyOrNullAmongOf(term, result, time)) {
                            result = result.replaceAll(",", "").replaceAll("\\+", "");
                            Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            Date date2 = TimeUtils.string2Date(nextTermTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                            String type = GameIndex.LotteryTypeCodeList.fc3d.getCode() + "";
                            PreSetResult preSetResult = preSetResultMapper.selectByTermAndType(type, term);
                            if (preSetResult != null && !Strings.isEmptyOrNullAmongOf(preSetResult.getCode())) {
                                result = preSetResult.getCode().replaceAll(",", "").replaceAll("\\|", "");
                            }
                            calFC3DOrder(baseUrl, roomId, term, result, date, date2, lotteryOpenBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return ReturnDataBuilder.makeBaseJSON(null);
    }

    private void calFC3DOrder(String baseUrl, String roomId, String term, String codes, Date time, Date nextTermTime, LotteryOpenBean lotteryOpenBean) {
        boolean isShouldAdd = false;

        if (lotteryOpenBean == null) {
            isShouldAdd = true;
        } else {
            if (!Strings.isEmptyOrNullAmongOf(lotteryOpenBean.getTerm()) && Integer.parseInt(term) > Integer.parseInt(lotteryOpenBean.getTerm())) {
                isShouldAdd = true;
            }
        }

        MyLog.l("福彩3D采集--->" + isShouldAdd + " " + term);
        if (!isShouldAdd) {
            return;
        }

        LotteryOpenBean newTermBean = new LotteryOpenBean();
        newTermBean.setCode(codes);
        newTermBean.setType(GameIndex.LotteryTypeCodeList.fc3d.getCode());
        newTermBean.setNextTerm(String.valueOf(Integer.parseInt(term) + 1));
        newTermBean.setTerm(term);
        newTermBean.setTime(time);

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
        params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.fc3d.getGame()));
        params.add(new PostParamBean("imgType", "robot"));
        params.add(new PostParamBean("username", "播报员"));
        params.add(new PostParamBean("betTerm", String.valueOf(Integer.parseInt(term) + 1)));

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append("/sendChat");

        HttpRequest.getInstance().post(sb.toString(), params);
    }


    //结算钱给客户
    private void calWinMoneyForPlayer() {
        //插入开奖号码后开始结算
        List<FC3DOrder> fc3DOrderList = fc3DOrderMapper.selectOrderByStatus(0);
        if (fc3DOrderList == null || fc3DOrderList.isEmpty()) {
            return;
        }


        Map<String, Float> map = new HashMap<>();
        List<FC3DOrder> fc3dOrderListCal = new ArrayList<>();
        for (FC3DOrder fc3DOrder : fc3DOrderList) {

            String openResultCodes = LotteryConfigGetter.getInstance().getOpenResultCodes(GameIndex.LotteryTypeCodeList.fc3d.getCode(), fc3DOrder.getTerm());
            if (Strings.isEmptyOrNullAmongOf(openResultCodes)) {
                MyLog.e("结算失败，获取开奖号码是空");
                continue;
            }

            int winTimes = 0;

            int gameType = -1;
            Map<Integer, String[]> playerBetCodesBeans = new HashMap<>();
            try {
                JSONObject data = new JSONObject(fc3DOrder.getContent());
                GameIndex.FC3DGameTypeCode typeCode = GameIndex.FC3DGameTypeCode.getFC3DGameTypeCode(data.optString("gameName", ""));
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

            //       rx2(1, "rx2", "任选二"),
            //        rx1(2, "rx1", "任选一"),
            //        d3(3, "d3", "3星直选"),
            //        d3c(4, "d3c", "3星组三"),
            //        d3c6(5, "d3c6", "3星组六"),
            //        d3c3sum(6, "d3c3sum", "3星组三和值"),
            //        d3c6sum(7, "d3c6sum", "3星组六和值"),
            //        d2f(8, "d2f", "2星前二直选"),
            //        d2b(9, "d2b", "2星后二直选"),
            //        d1(10, "d1", "定位胆"),
            //        dxds(11, "dxds", "大小单双");

            //结算算法
            switch (gameType) {
                case 1:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 2);
                    }
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 2:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 1);
                    }
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 3:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 4:
                    winTimes = FC3DCalWin.getInstance().calZu3(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 5:
                    winTimes = FC3DCalWin.getInstance().calZu6(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 6:
                    winTimes = FC3DCalWin.getInstance().calZu3HeZhi(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 7:
                    winTimes = FC3DCalWin.getInstance().calZu6HeZhi(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 8:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 9:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 1, 2);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 10:
                    winTimes = FixChooseCalWin.getInstance().calFixOneIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
                case 11:
                    winTimes = FixChooseCalWin.getInstance().calDXDS(openResultCodes, playerBetCodesBeans, 0, 1, 2);
                    sumBeforeWin(map, fc3DOrder, winTimes);
                    break;
            }

            fc3dOrderListCal.add(fc3DOrder);

        }

        //结算钱给客户
        winOrLost(map, fc3dOrderListCal);
    }

    private void sumBeforeWin(Map<String, Float> map, FC3DOrder fc3DOrder, int winTimes) {
        if (winTimes < 1) {
            fc3DOrder.setStatus(GameIndex.OrderCalculateStatus.lost.getCode());
            return;
        } else {
            fc3DOrder.setStatus(GameIndex.OrderCalculateStatus.win.getCode());
        }
        Float beforeWin = map.get(fc3DOrder.getUserid());
        float totalMoney = fc3DOrder.getUnitprice() * winTimes * fc3DOrder.getWinrate();
        if (beforeWin != null) {
            map.put(fc3DOrder.getUserid(), beforeWin + totalMoney);
        } else {
            map.put(fc3DOrder.getUserid(), totalMoney);
        }
    }

    private void winOrLost(Map<String, Float> map, List<FC3DOrder> fc3DOrderList) {
        //应该用事务的 但是数据库面目全非 算了
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            userBeanMapper.addUserMoney(BigDecimal.valueOf(entry.getValue()), entry.getKey());
        }

        //0 未结算 1赢了 2输了 9撤单
        for (int i = 0; i < fc3DOrderList.size(); i++) {
            FC3DOrder fc3DOrder = new FC3DOrder();
            fc3DOrder.setId(fc3DOrderList.get(i).getId());
            fc3DOrder.setStatus(fc3DOrderList.get(i).getStatus());
            fc3DOrderMapper.updateByPrimaryKeySelective(fc3DOrder);
        }

    }

    public Object getRemainTimeAndUser(String userId, String roomId, HttpServletRequest request) {
        Lottery21Setting lottery21Setting = LotteryConfigGetter.getInstance().getLottery21Setting();
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.fc3d.getCode());
        BigDecimal money = userBeanMapper.selectMoneyByUserId(userId, Integer.parseInt(roomId));

        if (!lottery21Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery21Setting.getFengtime();
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
        Lottery21Setting lottery21Setting = LotteryConfigGetter.getInstance().getLottery21Setting();
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.fc3d.getCode());

        if (!lottery21Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery21Setting.getFengtime();
        if (reamain < 0) {
            reamain = 0;
        }
        String remainTime = String.valueOf(reamain);
        Map<String, String> map = new HashMap<>();
        map.put("remainTime", remainTime);
        map.put("openTime", String.valueOf(lotteryOpenBean.getNextTime().getTime()));
        map.put("codes",lotteryOpenBean.getCode());
        map.put("term", lotteryOpenBean.getTerm());

        return ReturnDataBuilder.makeBaseJSON(map);
    }
}
