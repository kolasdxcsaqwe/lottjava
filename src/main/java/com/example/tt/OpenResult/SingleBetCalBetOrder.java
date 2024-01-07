package com.example.tt.OpenResult;

import com.example.tt.Bean.PL5Order;
import com.example.tt.utils.JSONArray;
import com.example.tt.utils.JSONException;
import com.example.tt.utils.JSONObject;
import com.example.tt.utils.Strings;

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

    public  int calOrder(int len,JSONArray jsonArray)
    {
        int order=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            String code=jsonArray.optString(i);
            if(code.length()==len && Strings.isDigitOnly(code))
            {
                order++;
            }
        }
        return order;
    }

    public List<PL5Order> converterOrderJson(int len, JSONArray jsonArray, PL5Order pl5Order)
    {
        List<PL5Order> pl5Orders=new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String code=jsonArray.optString(i);
            if(code.length()==len && Strings.isDigitOnly(code))
            {
                PL5Order temp=pl5Order.clone();
                try {
                    JSONObject original =new JSONObject(temp.getContent());
                    JSONArray codes=new JSONArray();
                    for (int j = 0; j < code.length(); j++) {
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("pos",j);
                            jsonObject.put("code",String.valueOf(code.charAt(j)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        codes.put(jsonObject);
                    }
                    original.put("codes",codes);
                    temp.setContent(original.toString());
                    pl5Orders.add(temp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return pl5Orders;
    }
}
