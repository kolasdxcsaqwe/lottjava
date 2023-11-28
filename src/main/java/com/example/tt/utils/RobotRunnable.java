package com.example.tt.utils;

import com.example.tt.Bean.PostParamBean;
import com.example.tt.Bean.RobotPlans;
import com.example.tt.Bean.RobotUser;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RobotRunnable implements Runnable {

    RobotPlans robotPlans;
    RobotUser robotUser;

    int betPeriod;

    public RobotRunnable(RobotPlans robotPlans, RobotUser robotUser, int betPeriod) {
        this.robotPlans = robotPlans;
        this.robotUser = robotUser;
        this.betPeriod = betPeriod;
    }

    @Override
    public void run() {
        if (betPeriod < 5) {
            betPeriod = 5;
        }
        int random = (int) (Math.random() * betPeriod) + 1;
        try {
            Thread.sleep(random*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(robotPlans.getContent());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    singlePlan(jsonObject.optString("gameType", ""), jsonObject.optString("moneyType", ""));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void singlePlan(String planType, String moneyType) {
        int betMoney = 0;
        String betContent = "";

        switch (moneyType) {
            case "money1"://10-200的随机金额，整数。
                betMoney = (int) (Math.random() * 191) + 10;
                break;
            case "money2"://100-1000的随机金额，整数。
                betMoney = (int) (Math.random() * 901) + 100;
                break;
            case "money3"://1000-15000的随机金额，整数。
                betMoney = (int) (Math.random() * 14001) + 1000;
                break;
        }

        switch (planType) {
            case "sjws"://随机位数

                break;
            case "sjtm"://随机特码

                break;
            case "sjsm"://随机双面
                betContent = sjsm(robotUser.getGame(), betMoney);
                break;
            case "sjlh"://随机龙虎
                betContent = sjlh(robotUser.getGame(), betMoney);
                break;
            case "smdds"://随机大单双
                betContent = smdds(robotUser.getGame(), betMoney);
                break;
            case "sjxds"://随机小单双
                betContent = sjxds(robotUser.getGame(), betMoney);
                break;
            case "sjsz"://随机数字

                break;
            case "sjts"://随机特殊

                break;
            case "sjhz"://随机和值
                betContent = sjhz(robotUser.getGame(), betMoney);
                break;
            case "sjlhpt"://随机六合平特

                break;
            case "sjlhhs"://随机六合号数

                break;

            case "sjlhbs"://随机六合波色

                break;
            case "sjlhdx"://随机六合单肖

                break;
            case "sjkstx"://随机快三通选

                break;
            case "sjkssj"://随机快三三军

                break;

            case "sjksbz"://随机快三豹子

                break;
            case "sjksdz"://随机快三对子

                break;
            case "sjkssz"://随机快三三杂

                break;
            case "sjksez"://随机快三二杂

                break;
        }

        List<PostParamBean> params=new ArrayList<>();
        params.add(new PostParamBean("content",betContent));
        params.add(new PostParamBean("userid",robotUser.getUserid()));
        params.add(new PostParamBean("gametype",robotUser.getGame()));
        params.add(new PostParamBean("headimg",robotUser.getHeadimg()));
        params.add(new PostParamBean("username",robotUser.getName()));
        params.add(new PostParamBean("roomid",robotUser.getRoomid()));

        HttpRequest.getInstance().post("http://localhost:8123/Application/ajax_chat_robot.php?type=send",params);
    }


    private String sjhz(String game, int money) {
        int val = rand(0, 27);
        StringBuilder sb=new StringBuilder();

        switch (game){
            case "xy28":
            case "jnd28":
            case "ny28":
                sb.append(val).append("/").append(money);
                break;
        }
        return sb.toString();
    }

    private String smdds(String game, int money) {
        StringBuilder sb=new StringBuilder();
        int val = rand(1, 2);
        String valStr = "";
        if (val == 1) {
            valStr = "大单";
        }
        else if(val == 2) {
            valStr = "大双";
        }

        switch (game){
            case "xy28":
            case "jnd28":
            case "ny28":
                sb.append(valStr).append(money);
                break;
            case "twk3":
                sb.append("总/").append(valStr).append("/").append(money);
                break;
        }

        return sb.toString();
    }

    private String sjxds(String game, int money) {
        StringBuilder sb=new StringBuilder();
        String valStr = "";
        int val = rand(1, 2);
        if (val == 1) {
            valStr = "小单";
        }
        else if(val == 2) {
            valStr = "小双";
        }

        switch (game){
            case "xy28":
            case "jnd28":
            case "ny28":
                sb.append(valStr).append(money);
                break;
            case "twk3":
                sb.append("总/").append(valStr).append("/").append(money);
                break;
        }

        return sb.toString();
    }

    private String sjsm(String game, int money) {
        StringBuilder sb=new StringBuilder();
        String valStr = "";
        int val = rand(1, 4);
        if (val == 1) {
            valStr = "大";
        }
        else if(val == 2) {
            valStr = "小";
        }
        else if(val == 3) {
            valStr = "单";
        }
        else if(val == 4) {
            valStr = "双";
        }

        int num = rand(1, 5);
        switch (game){
            case "jsssc":
                sb.append(num).append("/").append(valStr).append("/").append(money);
                break;
            case "twk3":
                sb.append("总/").append(valStr).append("/").append(money);
                break;
            case "xy28":
            case "jnd28":
            case "ny28":
                sb.append(valStr).append(money);
                break;
        }

        return sb.toString();
    }

    private String sjlh(String game, int money) {
        StringBuilder sb=new StringBuilder();
        String valStr = "";

        int val = rand(1, 2);
        if (val == 1) {
            valStr = "龙";
        }
        else if(val == 2) {
            valStr = "虎";
        }

        switch (game){
            case "jsssc":
                sb.append("/").append(valStr).append("/").append(money);
                break;
        }
        return sb.toString();
    }

    private int rand(int start, int end) {
        return (int) (Math.random() * (end - start + 1)) + start;
    }


}
