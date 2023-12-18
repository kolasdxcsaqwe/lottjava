package com.example.tt.Service;

import com.example.tt.Bean.*;
import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.OpenResult.FixChooseCalWin;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.dao.PL5OrderMapper;
import com.example.tt.dao.UserBeanMapper;
import com.example.tt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PL5Service {

    @Autowired(required = false)
    PL5OrderMapper pl5OrderMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    private static int check(JSONArray jsonArray, int type) {
        int orderAmount = 0;

        if (jsonArray == null || jsonArray.length() < 1) {
            return 0;
        }

        //都是数字 大小单双用0123 代替
        int mul = 1;
        List<String> stringList = new ArrayList<>();
        Map<Integer, String> codes = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String str = jsonObject.optString("code", "").trim().replaceAll(" ", "");
            codes.put(jsonObject.optInt("pos", -1), str);
            if (str.isEmpty() || !Strings.isDigitOnly(str)) {
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
                orderAmount = FixChooseCalWin.checkFormatFixPosition(codes, 0, 1, 2, 3,4) ? mul : 0;
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

                break;
            case 8:

                break;
        }

        return orderAmount;
    }

    public Object betPL5(String betArray, String userId, String roomId, HttpServletRequest request)
    {
        Lottery22Setting lottery22Setting = LotteryConfigGetter.getInstance().getLottery22Setting();
        int fengTime = lottery22Setting.getFengtime();

        LotteryOpenBean lotteryOpenBean = lotteryOpenBeanMapper.getLastOpenData(GameIndex.LotteryTypeCodeList.qxc.getCode());
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

    public String cancelOrder(String id)
    {
        if(!Strings.isDigitOnly(id))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        PL5Order qxcOrder = new PL5Order();
        qxcOrder.setId(Integer.parseInt(id));
        qxcOrder.setStatus(GameIndex.OrderCalculateStatus.quit.getCode());
        int status=pl5OrderMapper.updateByPrimaryKey(qxcOrder);

        return ReturnDataBuilder.returnData(status>0);
    }
}
