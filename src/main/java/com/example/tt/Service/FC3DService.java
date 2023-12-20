package com.example.tt.Service;

import com.example.tt.Bean.FC3DOrder;
import com.example.tt.Bean.Lottery21Setting;
import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.OpenResult.FixChooseCalWin;
import com.example.tt.OpenResult.NiuNiuCalWin;
import com.example.tt.dao.*;
import com.example.tt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

//    final String fc3dUrl="https://api.api68.com/QuanGuoCai/getLotteryInfo1.do?lotCode=10041";//福彩3D开奖地址

    final String fc3dUrl = "http://localhost:8653/fakeOpenResult?lotteryName=fc3d";//假排列5开奖地址


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
            String[] nums=str.split(",");
            codes.put(jsonObject.optInt("pos", -1), nums);
            if (str.isEmpty()) {
                return 0;
            } else {
                mul = mul * nums.length;
            }
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
                orderAmount =mul>1?mul*(mul-1):0;
                break;
            case 5:
                orderAmount =mul>2?mul*(mul-1)*(mul-2):0;
                break;
            case 6:
                orderAmount = AnyChooseCalWin.checkFormatAnyPosition(codes, 0, 1, 2, 3) ? mul : 0;
                break;
            case 7:
                orderAmount= NiuNiuCalWin.getInstance().calNiu(codes,0);
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

    public String cancelOrder(String id)
    {
        if(!Strings.isDigitOnly(id))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        FC3DOrder fc3DOrder = new FC3DOrder();
        fc3DOrder.setId(Integer.parseInt(id));
        fc3DOrder.setStatus(GameIndex.OrderCalculateStatus.quit.getCode());
        int status=fc3DOrderMapper.updateByPrimaryKey(fc3DOrder);

        return ReturnDataBuilder.returnData(status>0);
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


    public Object fetchFC3DResult(final String roomId, final String baseUrl) {

        return null;
    }
}
