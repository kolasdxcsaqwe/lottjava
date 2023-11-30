package com.example.tt.utils;

import java.util.HashMap;
import java.util.Map;

public class ReturnDataBuilder {

    public enum  GameListNameEnum {
        S0(0, "操作成功"),
        S1(-1, "彩种错误"),
        S2(-2, "参数错误"),
        S3(-3, "无此用户"),
        S4(-4, "缺少头像"),
        S5(-3, "发送失败");

        private int code;
        private String msg;

        GameListNameEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static String error(GameListNameEnum gameListNameEnum)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("msg",gameListNameEnum.msg);
            jsonObject.put("code",gameListNameEnum.code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Map error(int code ,String msg)
    {
        Map map=new HashMap();
        map.put("msg",msg);
        map.put("code",code);
        return map;
    }

    public static Map error(int code ,String msg,Object object)
    {
        Map map=new HashMap();
        map.put("msg",msg);
        map.put("code",code);
        if(object!=null)
        {
            map.put("datas",object);
        }
        return map;
    }

    public static Map makeBaseJSON(Object object)
    {
        Map map=new HashMap();
        map.put("msg","操作成功");
        map.put("code",0);
        if(object!=null)
        {
            map.put("datas",object);
        }
        return map;
    }

}
