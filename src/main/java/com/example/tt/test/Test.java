package com.example.tt.test;

import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.utils.JSONArray;
import com.example.tt.utils.JSONException;
import com.example.tt.utils.JSONObject;
import com.example.tt.utils.Strings;

import java.util.*;

public class Test {

    public static Stack<Character> stack = new Stack<Character>();

    static int win = 0;
    static int count=0;

    public static void main(String[] args) {
        String kai = "12345";
        String bet = "1924530";
        int type = 2;// 选 2
//        System.err.println(removeSameNum(kai));
        System.err.println("方式数--》" + calculateOrderAnyChoose(removeSameNum(kai).length(), type));
//
//        System.err.println("WIN--->" + AnyChooseCalWin.getInstance().getWinTimes(kai, bet, type));

//        JSONObject jsonObject=new JSONObject();
//        try {
//            jsonObject.put("gamName","ry3");
//            jsonObject.put("orderPrice",6);
//            JSONArray jsonArray=new JSONArray();
//            jsonArray.put(makeJsonObj("0","453156"));
//            jsonArray.put(makeJsonObj("4","4342"));
//            jsonObject.put("code",jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.err.println(jsonObject.toString());
        //豹子 10/220 顺子8/220 对子
        cal("1234567890","123",3,0,0);
        System.err.println("count-->"+count);
    }

    private static JSONObject makeJsonObj(String pos,String codes)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("pos",pos);
            jsonObject.put("codes",codes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     *
     * @param shu   元素
     * @param targ  要选多少个元素
     * @param has   当前有多少个元素
     * @param cur   当前选到的下标
     * 1    2   3     //开始下标到2
     * 1    2   4     //然后从3开始
     */
    private static void cal(String shu, String bet, int targ, int has, int cur) {
        if(has == targ) {
            System.err.println(stack);
            count++;
            for (int i = 0; i < stack.size(); i++) {
                boolean hasChar= Strings.hasContainsChar(bet,stack.get(i));
                if(hasChar)
                {
                    if(i==stack.size()-1)
                    {
                        win++;
                    }
                }
                else
                {
                    break;
                }
            }
            return;
        }

        for(int i=cur;i<shu.length();i++) {
            if(!stack.contains(shu.charAt(i))) {
                stack.add(shu.charAt(i));
                cal(shu, bet,targ, has+1, i);
                stack.pop();
            }
        }

    }

    private static String removeSameNum(String num) {
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        for (int i = 0; i < num.length(); i++) {
            boolean isSame = false;
            for (int j = i + 1; j < num.length(); j++) {
                if (num.charAt(i) == num.charAt(j)) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                sb.append(num.charAt(i));
            }
        }
        return sb.toString();
    }

    private static boolean hasContainChar(String target, char arg) {
        boolean hasInKai = false;
        for (int j = 0; j < target.length(); j++) {
            if (target.charAt(j) == arg) {
                hasInKai = true;
            }
        }
        return hasInKai;
    }

    //choose 用户选中 当前彩种任选几
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
}
