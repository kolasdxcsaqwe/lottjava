package com.example.tt.OpenResult;

import com.example.tt.Bean.FC3DOrder;
import com.example.tt.Bean.PL5Order;
import com.example.tt.Bean.QXCOrder;
import com.example.tt.utils.*;

import java.util.ArrayList;
import java.util.List;

public class SingleBetCalBetOrder {

    static SingleBetCalBetOrder singleBetCalBetOrder;

    public static SingleBetCalBetOrder getInstance() {
        if (singleBetCalBetOrder == null) {
            singleBetCalBetOrder = new SingleBetCalBetOrder();
        }
        return singleBetCalBetOrder;
    }

    public int calOrder(int len, JSONArray jsonArray) {
        int order = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            String code = jsonArray.optString(i);
            if (code.length() == len && Strings.isDigitOnly(code)) {
                order++;
            }
        }
        return order;
    }

    public List<PL5Order> converterOrderJson(int len, JSONArray jsonArray, PL5Order pl5Order) {
        List<PL5Order> pl5Orders = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String code = jsonArray.optString(i);
            if (code.length() == len && Strings.isDigitOnly(code)) {
                PL5Order temp = pl5Order.clone();
                try {
                    JSONObject original = new JSONObject(temp.getContent());
                    JSONArray codes = new JSONArray();
                    for (int j = 0; j < code.length(); j++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("pos", j);
                            jsonObject.put("code", String.valueOf(code.charAt(j)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        codes.put(jsonObject);
                    }
                    original.put("codes", codes);
                    temp.setContent(original.toString());
                    pl5Orders.add(temp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return pl5Orders;
    }

    public List<QXCOrder> converterOrderJson(int len, JSONArray jsonArray, QXCOrder qxcOrder) {
        List<QXCOrder> qxcOrders = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String code = jsonArray.optString(i);
            if (code.length() == len && Strings.isDigitOnly(code)) {
                QXCOrder temp = qxcOrder.clone();
                try {
                    JSONObject original = new JSONObject(temp.getContent());
                    JSONArray codes = new JSONArray();
                    for (int j = 0; j < code.length(); j++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("pos", j);
                            jsonObject.put("code", String.valueOf(code.charAt(j)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        codes.put(jsonObject);
                    }
                    original.put("codes", codes);
                    temp.setContent(original.toString());
                    qxcOrders.add(temp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return qxcOrders;
    }

    public List<FC3DOrder> converterOrderJson(int len, JSONArray jsonArray, FC3DOrder fc3DOrder, GameIndex.FC3DGameTypeCode fc3DGameTypeCode) {
        List<FC3DOrder> fc3DOrderList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String code = jsonArray.optString(i);
            if (code.length() == len && Strings.isDigitOnly(code)) {
                FC3DOrder temp = fc3DOrder.clone();
                try {
                    JSONObject original = new JSONObject(temp.getContent());
                    JSONArray codes = new JSONArray();
                    for (int j = 0; j < code.length(); j++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("pos", fc3DGameTypeCode.getCode()==9?j+1:j);
                            jsonObject.put("code", String.valueOf(code.charAt(j)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        codes.put(jsonObject);
                    }
                    original.put("codes", codes);

                    temp.setContent(original.toString());
                    fc3DOrderList.add(temp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fc3DOrderList;
    }
}
