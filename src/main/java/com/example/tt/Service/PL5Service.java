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
public class PL5Service {

    @Autowired(required = false)
    PL5OrderMapper pl5OrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    @Autowired(required = false)
    MarkLogMapper markLogMapper;

    @Autowired(required = false)
    PreSetResultMapper preSetResultMapper;

    //    final String pl5Url="https://api.api68.com/QuanGuoCai/getLotteryInfo.do?lotCode=10044";//排列5开奖地址

    final String pl5Url = "http://localhost:8653/fakeOpenResult?lotteryName=pl5";//假排列5开奖地址

    final String[] titles={"万位","千位","百位","十位","个位"};

    private static int check(boolean isMultiBet,JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        Map<Integer, String[]> codes = new HashMap<>();
        if(isMultiBet)
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String str = jsonObject.optString("code", "").trim().replaceAll(" ", "");
                String[] nums = str.split(",");
                codes.put(jsonObject.optInt("pos", -1), nums);
                if (nums.length == 0) {
                    return 0;
                } else {
                    mul = mul * nums.length;
                }
            }
        }

        if (isMultiBet && codes.isEmpty()) {
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
                if(isMultiBet)
                {
                    orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2, 3, 4) ? mul : 0;
                }
                else
                {
                    orderAmount = SingleBetCalBetOrder.getInstance().calOrder(5,jsonArray);
                }
                break;
            case 4:
                if(isMultiBet)
                {
                    orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2) ? mul : 0;
                }
                else
                {
                    orderAmount = SingleBetCalBetOrder.getInstance().calOrder(3,jsonArray);
                }
                break;
            case 5:
                if(isMultiBet)
                {
                    orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1) ? mul : 0;
                }
                else
                {
                    orderAmount = SingleBetCalBetOrder.getInstance().calOrder(2,jsonArray);
                }
                break;
            case 6:
                orderAmount = AnyChooseCalWin.checkFormatAnyPosition(codes, 0, 1, 2, 3,4) ? mul : 0;
                break;
            case 7:
                orderAmount = NiuNiuCalWin.getInstance().calNiu(codes, 0);
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
            case 9:
                orderAmount = mul > 1 ? mul * (mul - 1) : 0;
                break;
            case 10:
                orderAmount = mul > 2 ? mul * (mul - 1) * (mul - 2) / 6 : 0;
                break;
        }

        return orderAmount;
    }

    private float getWinRate(int gameType, Lottery22Setting lottery22Setting) {
        float rate = 0.0f;
        switch (gameType) {
            case 1:
                rate = lottery22Setting.getAnythree();
                break;
            case 2:
                rate = lottery22Setting.getAnytwo();
                break;
            case 3:
                rate = lottery22Setting.getFivefix();
                break;
            case 4:
                rate = lottery22Setting.getThreefix();
                break;
            case 5:
                rate = lottery22Setting.getTwofix();
                break;
            case 6:
                rate = lottery22Setting.getOnefix();
                break;
            case 7:
                //斗牛要重新算
                rate = 0;
                break;
            case 8:
                rate = lottery22Setting.getDxds();
                break;
            case 9:
                rate = lottery22Setting.getCombinethree();
                break;
            case 10:
                rate = lottery22Setting.getCombinesix();
                break;
        }
        return rate;
    }


    public Object betPL5(String betArray, String userId, String roomId, HttpServletRequest request) {
        Lottery22Setting lottery22Setting = LotteryConfigGetter.getInstance().getLottery22Setting(false);
        int fengTime = lottery22Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.pl5.getCode());
        if (!lottery22Setting.getGameopen() || lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
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
                int singleOrderMoney = jsonObject.optInt("money", 0);
                boolean combineChatContent = jsonObject.optBoolean("combineChatContent", false);
                boolean isMultiBet = jsonObject.optBoolean("isMultiBet", true);

                if (unitPrice < 1 || Strings.isEmptyOrNullAmongOf(gameName)) {
                    isFormatOk = false;
                }

                GameIndex.PL5GameTypeCode pl5GameTypeCode = GameIndex.PL5GameTypeCode.getPL5GameTypeCode(gameName);
                if (pl5GameTypeCode == null) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                if (unitPrice < lottery22Setting.getMinbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery22Setting.getMinbet()));
                }

                if (unitPrice > lottery22Setting.getMaxbet()) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S21, String.valueOf(lottery22Setting.getMinbet()));
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(isMultiBet,codes, pl5GameTypeCode.getCode());
                StringBuilder chatContent = new StringBuilder();
                chatContent.append(Strings.makeBoldSpan(pl5GameTypeCode.getExplain(),"#ec4127","4rem"));
                chatContent.append("<br>");

                if(combineChatContent)
                {
                    chatContent.append(codes.toString());
                }
                else
                {
                    for (int j = 0; j < codes.length(); j++) {
                        JSONObject temp = codes.optJSONObject(j);

                        int pos=temp.optInt("pos",-1);
                        if(pl5GameTypeCode.getLine()>1 && pos>-1 && pos<titles.length)
                        {
                            chatContent.append(Strings.makeSpan(titles[pos]+":","red","4rem"));
                        }
                        if(pl5GameTypeCode.getCode()==GameIndex.PL5GameTypeCode.dxds.getCode())
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
                        else if(pl5GameTypeCode.getCode()==GameIndex.PL5GameTypeCode.dn.getCode())
                        {
                            String[] codesArray=temp.optString("code","").split(",");
                            if(codesArray.length>0)
                            {
                                for(String str:codesArray)
                                {
                                    int num=str.charAt(0)-'0';
                                    if(GameIndex.douNiuTitles.length>num)
                                    {
                                        chatContent.append(GameIndex.douNiuTitles[num]).append(",");
                                    }
                                }
                                if(chatContent.length()>0)
                                {
                                    chatContent.deleteCharAt(chatContent.length()-1);
                                }
                                chatContent.append("<br>");
                            }
                        }
                        else
                        {
                            chatContent.append(temp.optString("code","")).append("<br>");
                        }
                    }
                }

                if (orderAmount < 1) {
                    isFormatOk = false;
                }

                if (orderAmount>100) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S22);
                }

                if (orderAmount > 1 && singleOrderMoney % orderAmount != 0) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S15);
                }

                if (!isFormatOk) {
                    return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S10);
                }

                //加入行数字段
                jsonObject.put("lines",pl5GameTypeCode.getLine());

                int status =0;
                float winRate = getWinRate(pl5GameTypeCode.getCode(), lottery22Setting);
                PL5Order pl5Order = new PL5Order();
                pl5Order.setAddtime(Calendar.getInstance().getTime());
                pl5Order.setContent(jsonObject.toString());
                pl5Order.setTerm(lotteryOpenBean.getNextTerm());
                pl5Order.setMoney(unitPrice * orderAmount);
                pl5Order.setStatus(GameIndex.OrderCalculateStatus.unCalculate.getCode());
                pl5Order.setGamename(GameIndex.LotteryTypeCodeList.pl5.getGame());
                pl5Order.setGametype(GameIndex.LotteryTypeCodeList.pl5.getCode());
                pl5Order.setOrderamount(orderAmount);
                pl5Order.setUnitprice(unitPrice);
                pl5Order.setWinrate(winRate);
                pl5Order.setUsername(userBean.getUsername());
                pl5Order.setHeadimg(userBean.getHeadimg());
                pl5Order.setMingci("");
                pl5Order.setJia(userBean.getJia());
                pl5Order.setUserid(userId);
                pl5Order.setRoomid(Integer.parseInt(roomId));

                if(isMultiBet)
                {
                    status =pl5OrderMapper.insertSelective(pl5Order);
                }
                else
                {
                    int len=0;
                    switch (pl5GameTypeCode.getCode())
                    {
                        case 3:
                            len=5;
                            break;
                        case 4:
                            len=3;
                            break;
                        case 5:
                            len=2;
                            break;
                    }
                    status =pl5OrderMapper.insertOrderList(SingleBetCalBetOrder.getInstance().converterOrderJson(len,codes,pl5Order));
                }


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
                    params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.pl5.getGame()));
                    params.add(new PostParamBean("betTerm", lotteryOpenBean.getNextTerm()));
                    params.add(new PostParamBean("headimg", pl5Order.getHeadimg()));
                    params.add(new PostParamBean("username", pl5Order.getUsername()));

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
                markLog.setContent(GameIndex.LotteryTypeCodeList.pl5.getExplain() + "投注");
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
                boolean combineChatContent = jsonObject.optBoolean("combineChatContent", false);
                boolean isMultiBet = jsonObject.optBoolean("isMultiBet", true);

                GameIndex.PL5GameTypeCode pl5GameTypeCode = GameIndex.PL5GameTypeCode.getPL5GameTypeCode(gameName);
                if (pl5GameTypeCode == null) {
                    return -1;
                }

                JSONArray codes = jsonObject.optJSONArray("codes");
                int orderAmount = check(isMultiBet,codes, pl5GameTypeCode.getCode());
                orderTotalMoney = orderTotalMoney + (unitPrice * orderAmount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderTotalMoney;
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

    public String cancelOrder(String id) {
        if (!Strings.isDigitOnly(id)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        PL5Order pl5Order = new PL5Order();
        pl5Order.setId(Integer.parseInt(id));
        pl5Order.setStatus(GameIndex.OrderCalculateStatus.quit.getCode());
        int status = pl5OrderMapper.updateByPrimaryKeySelective(pl5Order);

        return ReturnDataBuilder.returnData(status > 0);
    }

    public Object fetchPL5Result(final String roomId, final String baseUrl) {

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.pl5.getCode());
        if (lotteryOpenBean != null && lotteryOpenBean.getNextTime() != null) {
            if (System.currentTimeMillis() < lotteryOpenBean.getNextTime().getTime()) {
                return ReturnDataBuilder.error(92323, "未到开奖时间");
            }
        }

        HttpRequest.getInstance().get(pl5Url, null, new HttpCallBack() {
            @Override
            public void onError(Exception ex) {
                MyLog.e("排列5开奖结果请求失败");
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
                        if (resultSplit.length < 5) {
                            MyLog.e("排列5开奖结果错误");
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 5; i++) {
                            sb.append(resultSplit[i]);
                        }
//                        sb.append(",").append(resultSplit[resultSplit.length-1]);
                        result = sb.toString();

                        if (!Strings.isEmptyOrNullAmongOf(term, result, time)) {
                            result = result.replaceAll(",", "").replaceAll("\\+", "");
                            Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            Date date2 = TimeUtils.string2Date(nextTermTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                            String type = GameIndex.LotteryTypeCodeList.pl5.getCode() + "";
                            PreSetResult preSetResult = preSetResultMapper.selectByTermAndType(type, term);
                            if (preSetResult != null && !Strings.isEmptyOrNullAmongOf(preSetResult.getCode())) {
                                result = preSetResult.getCode().replaceAll(",", "").replaceAll("\\|", "");
                            }
                            calPL5Order(baseUrl, roomId, term, result, date, date2, lotteryOpenBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return ReturnDataBuilder.makeBaseJSON(null);
    }

    private void calPL5Order(String baseUrl, String roomId, String term, String codes, Date time, Date nextTermTime, LotteryOpenBean lotteryOpenBean) {
        boolean isShouldAdd = false;

        if (lotteryOpenBean == null) {
            isShouldAdd = true;
        } else {
            if (!Strings.isEmptyOrNullAmongOf(lotteryOpenBean.getTerm()) && Integer.parseInt(term) > Integer.parseInt(lotteryOpenBean.getTerm())) {
                isShouldAdd = true;
            }
        }

        MyLog.l("排列5采集--->" + isShouldAdd + " " + term);
        if (!isShouldAdd) {
            return;
        }

        LotteryOpenBean newTermBean = new LotteryOpenBean();
        newTermBean.setCode(codes);
        newTermBean.setType(GameIndex.LotteryTypeCodeList.pl5.getCode());
        newTermBean.setNextTerm(String.valueOf(Integer.parseInt(term) + 1));
        newTermBean.setTerm(term);
        newTermBean.setTime(time);
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
        params.add(new PostParamBean("game", GameIndex.LotteryTypeCodeList.pl5.getGame()));
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
        List<PL5Order> pl5OrderList = pl5OrderMapper.selectOrderByStatus(0);
        if (pl5OrderList == null || pl5OrderList.isEmpty()) {
            return;
        }


        Map<String, Float> map = new HashMap<>();
        List<PL5Order> pl5OrderListCal = new ArrayList<>();
        for (PL5Order pl5Order : pl5OrderList) {

            String openResultCodes = LotteryConfigGetter.getInstance().getOpenResultCodes(GameIndex.LotteryTypeCodeList.pl5.getCode(), pl5Order.getTerm());
            if (Strings.isEmptyOrNullAmongOf(openResultCodes)) {
                MyLog.e("第"+pl5Order.getTerm()+"期,结算失败，获取开奖号码是空");
                continue;
            }



            int gameType = -1;
            Map<Integer, String[]> playerBetCodesBeans = new HashMap<>();
            try {
                JSONObject data = new JSONObject(pl5Order.getContent());
                GameIndex.PL5GameTypeCode typeCode = GameIndex.PL5GameTypeCode.getPL5GameTypeCode(data.optString("gameName", ""));
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

            int winTimes = 0;
            //结算算法
            switch (gameType) {
                case 1:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 3);
                    }
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 2:
                    if (!playerBetCodesBeans.isEmpty() && playerBetCodesBeans.get(0) != null && !Strings.isEmptyOrNullAmongOf(playerBetCodesBeans.get(0))) {
                        winTimes = AnyChooseCalWin.getInstance().getWinTimes(openResultCodes, playerBetCodesBeans.get(0), 2);
                    }
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 3:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3, 4);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 4:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 5:
                    winTimes = FixChooseCalWin.getInstance().calFixIsWin(openResultCodes, playerBetCodesBeans, 0, 1);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 6:
                    winTimes = FixChooseCalWin.getInstance().calFixOneIsWin(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 7:
                    //斗牛排序 无牛到牛牛
                    winTimes = NiuNiuCalWin.getInstance().calNiu(openResultCodes, playerBetCodesBeans);
                    int niuWhat=NiuNiuCalWin.getInstance().syncBull(openResultCodes);
                    sumBeforeWinNiuNiu(niuWhat,map, pl5Order, winTimes);
                    break;
                case 8:
                    winTimes = FixChooseCalWin.getInstance().calDXDS(openResultCodes, playerBetCodesBeans, 0, 1, 2, 3,4);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 9:
                    winTimes = FC3DCalWin.getInstance().calZu3(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
                case 10:
                    winTimes = FC3DCalWin.getInstance().calZu6(openResultCodes, playerBetCodesBeans);
                    sumBeforeWin(gameType,map, pl5Order, winTimes);
                    break;
            }

            pl5OrderListCal.add(pl5Order);

        }

        //结算钱给客户
        winOrLost(map, pl5OrderListCal);
    }

    private void sumBeforeWinNiuNiu(int niuWhat,Map<String, Float> map, PL5Order pl5Order, int winTimes) {
        if (winTimes < 1) {
            pl5Order.setStatus(GameIndex.OrderCalculateStatus.lost.getCode());
            return;
        } else {
            pl5Order.setStatus(GameIndex.OrderCalculateStatus.win.getCode());
        }
        Float beforeWin = map.get(pl5Order.getUserid());

        Lottery22Setting lottery22Setting = LotteryConfigGetter.getInstance().getLottery22Setting(false);
        if(niuWhat==0)
        {
            //无牛
            pl5Order.setWinrate(lottery22Setting.getWuniu());
        }
        else
        {
            //有牛
            pl5Order.setWinrate(lottery22Setting.getYouniu());
        }

        float totalMoney = pl5Order.getUnitprice() * winTimes * pl5Order.getWinrate();
        pl5Order.setWinmoney(totalMoney);
        if (beforeWin != null) {
            map.put(pl5Order.getUserid(), beforeWin + totalMoney);
        } else {
            map.put(pl5Order.getUserid(), totalMoney);
        }
    }

    private void sumBeforeWin(int gameType,Map<String, Float> map, PL5Order pl5Order, int winTimes) {
        if (winTimes < 1) {
            pl5Order.setStatus(GameIndex.OrderCalculateStatus.lost.getCode());
            return;
        } else {
            pl5Order.setStatus(GameIndex.OrderCalculateStatus.win.getCode());
        }
        Float beforeWin = map.get(pl5Order.getUserid());


        float totalMoney = pl5Order.getUnitprice() * winTimes * pl5Order.getWinrate();
        pl5Order.setWinmoney(totalMoney);
        if (beforeWin != null) {
            map.put(pl5Order.getUserid(), beforeWin + totalMoney);
        } else {
            map.put(pl5Order.getUserid(), totalMoney);
        }
    }

    private void winOrLost(Map<String, Float> map, List<PL5Order> pl5OrderList) {
        //应该用事务的 但是数据库面目全非 算了
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            userBeanMapper.addUserMoney(BigDecimal.valueOf(entry.getValue()), entry.getKey());
        }

        //0 未结算 1赢了 2输了 9撤单
        for (int i = 0; i < pl5OrderList.size(); i++) {
            PL5Order pl5Order = new PL5Order();
            pl5Order.setId(pl5OrderList.get(i).getId());
            pl5Order.setStatus(pl5OrderList.get(i).getStatus());
            pl5Order.setWinrate(pl5OrderList.get(i).getWinrate());
            pl5Order.setWinmoney(pl5OrderList.get(i).getWinmoney());
            pl5OrderMapper.updateByPrimaryKeySelective(pl5Order);
        }

    }

    public Object getRemainTimeAndUser(String userId, String roomId, HttpServletRequest request) {
        Lottery22Setting lottery22Setting = LotteryConfigGetter.getInstance().getLottery22Setting(false);
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.pl5.getCode());
        BigDecimal money = userBeanMapper.selectMoneyByUserId(userId, Integer.parseInt(roomId));

        if (!lottery22Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery22Setting.getFengtime();
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
        Lottery22Setting lottery22Setting = LotteryConfigGetter.getInstance().getLottery22Setting(false);
        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.pl5.getCode());

        if (!lottery22Setting.getGameopen()) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S11);
        }

        if (lotteryOpenBean == null || lotteryOpenBean.getNextTime() == null) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S18);
        }

        long reamain = lotteryOpenBean.getNextTime().getTime() / 1000 - (System.currentTimeMillis() / 1000) - lottery22Setting.getFengtime();
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
}
