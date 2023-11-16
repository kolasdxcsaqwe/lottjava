package com.example.tt.utils;

public class ReturnDataBuilder {

    public enum  GameListNameEnum {
        S0(0, "操作成功"),
        S1(-1, "彩种错误"),
        S2(-2, "参数错误");

        private int code;
        private String msg;

        GameListNameEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static JSONObject error(GameListNameEnum gameListNameEnum)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("msg",gameListNameEnum.msg);
            jsonObject.put("code",gameListNameEnum.code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject error(int code ,String msg)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("msg",msg);
            jsonObject.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject makeBaseJSON(Object object)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("msg","操作成功");
            jsonObject.put("code",0);
            jsonObject.put("datas",object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
