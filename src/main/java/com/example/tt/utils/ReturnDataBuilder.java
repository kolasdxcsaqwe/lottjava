package com.example.tt.utils;

import java.util.HashMap;
import java.util.Map;

public class ReturnDataBuilder {

    public enum GameListNameEnum {
        S0(0, "操作成功"),
        S1(-1, "彩种错误"),
        S2(-2, "参数错误"),
        S3(-3, "无此用户"),
        S4(-4, "缺少头像"),
        S5(-5, "发送失败"),
        S6(-6, "警告:投注格式不正确或已经封盘"),
        S7(-7, "您已经被该房间禁言！无法发言与投注！"),
        S8(-8, "您发送的内容内含有屏蔽词汇,请删除后重试."),
        S9(-9, "操作失败"),
        S10(-10, "投注格式不正确"),
        S11(-11, "已经封盘请等待下一期"),
        S12(-12, "该用户名已经被注册，请更换用户名"),
        S13(-13, "用户名或者密码不正确"),
        S14(-14, "登录过期，请重新登录"),
        S15(-15, "复试投注金额需要是注数的倍数"),
        S16(-16, "查无此用户"),
        S17(-17, "余额不足支付注单，请联系客服上分"),
        S18(-18, "该彩种查询结果为空,需要打开获取开奖"),

        S19(-19, "当前无注单,请下注"),
        S20(-20, "单注不得小于%s元"),
        S21(-21, "单注不得大于%s元");

        private int code;
        private String msg;

        GameListNameEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static String error(GameListNameEnum gameListNameEnum,Object...args) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", String.format(gameListNameEnum.msg,args));
            jsonObject.put("code", gameListNameEnum.code);
            jsonObject.put("success", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String error(GameListNameEnum gameListNameEnum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", gameListNameEnum.msg);
            jsonObject.put("code", gameListNameEnum.code);
            jsonObject.put("success", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Map error(int code, String msg) {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        map.put("success", "false");
        return map;
    }

    public static Map error(int code, String msg, Object object) {
        Map map = new HashMap();
        map.put("msg", msg);
        map.put("code", code);
        map.put("success", "false");
        if (object != null) {
            map.put("datas", object);
        }
        return map;
    }

    public static Map makeBaseJSON(Object object) {
        Map map = new HashMap();
        map.put("msg", "操作成功");
        map.put("code", 0);
        map.put("success", "true");
        if (object != null) {
            map.put("datas", object);
        }
        return map;
    }

    public static JSONObject makeJSON(JSONObject jsonObject) {

        if (jsonObject != null) {
            JSONObject json=new JSONObject();
            try {
                json.put("msg", "操作成功");
                json.put("code", 0);
                json.put("success", "true");
                json.put("datas", jsonObject);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return json;
        }
        return jsonObject;
    }

    public static JSONObject makeJSON(JSONArray jsonArray) {

        if (jsonArray != null) {
            JSONObject json=new JSONObject();
            try {
                json.put("msg", "操作成功");
                json.put("code", 0);
                json.put("success", "true");
                json.put("datas", jsonArray);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return json;
        }

        return null;
    }

    public static String returnData(boolean isSuccess) {

        if(isSuccess)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("msg", GameListNameEnum.S0);
                jsonObject.put("code", GameListNameEnum.S0.code);
                jsonObject.put("success", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
        else
        {
            return error(GameListNameEnum.S9);
        }

    }

}
