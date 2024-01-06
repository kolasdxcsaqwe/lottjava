package com.example.tt.OpenResult;

import java.util.Map;

public class FC3DCalWin {

    static FC3DCalWin fc3DCalWin;

    public static FC3DCalWin getInstance() {
        if (fc3DCalWin == null) {
            fc3DCalWin = new FC3DCalWin();
        }
        return fc3DCalWin;
    }

    //组3
    public int calZu3(String openResult, Map<Integer,String[]> map)
    {

        if(map==null || map.isEmpty() || openResult.length()<3)
        {
            return 0;
        }

        String[] betNums=map.get(0);

        if(openResult.charAt(0)==openResult.charAt(1) && openResult.charAt(2)==openResult.charAt(1))
        {
            //豹子不算
            return 0;
        }

        if(openResult.charAt(0)==openResult.charAt(1) || openResult.charAt(2)==openResult.charAt(1)
                || openResult.charAt(2)==openResult.charAt(0))
        {
            //对子
            int count=0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < betNums.length; j++) {
                    if(betNums[j].charAt(0)==openResult.charAt(i))
                    {
                        count++;
                    }
                }
            }
            if(count>2)
            {
                return 1;
            }
        }

        return 0;
    }

    //组6
    public int calZu6(String openResult, Map<Integer,String[]> map)
    {

        if(map==null || map.isEmpty() || openResult.length()<3)
        {
            return 0;
        }

        if(openResult.charAt(0)==openResult.charAt(1) || openResult.charAt(2)==openResult.charAt(1)
                || openResult.charAt(2)==openResult.charAt(0))
        {
            //不能有相同的
            return 0;
        }

        String[] betNums=map.get(0);

        int count=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < betNums.length; j++) {
                if(betNums[j].charAt(0)==openResult.charAt(i))
                {
                    count++;
                }
            }
        }
        if(count>2)
        {
            return 1;
        }

        return 0;
    }

    //组3和值
    public int calZu3HeZhi(String openResult, Map<Integer,String[]> map)
    {
        if(map==null || map.isEmpty() || openResult.length()<3)
        {
            return 0;
        }

        String[] betNums=map.get(0);
        int resultCodeSum=openResult.charAt(0)+openResult.charAt(1)+openResult.charAt(2)-'0'-'0'-'0';

        if(openResult.charAt(0)==openResult.charAt(1) && openResult.charAt(2)==openResult.charAt(1))
        {
            //豹子不算
            return 0;
        }

        if(openResult.charAt(0)==openResult.charAt(1) || openResult.charAt(2)==openResult.charAt(1)
                || openResult.charAt(2)==openResult.charAt(0))
        {
            for (String betNum : betNums) {
                if (resultCodeSum == Integer.parseInt(betNum)) {
                    return 1;
                }
            }
        }

        return 0;
    }

    //组6和值
    public int calZu6HeZhi(String openResult, Map<Integer,String[]> map)
    {
        if(map==null || map.isEmpty() || openResult.length()<3)
        {
            return 0;
        }

        String[] betNums=map.get(0);
        int resultCodeSum=openResult.charAt(0)+openResult.charAt(1)+openResult.charAt(2)-'0'-'0'-'0';

        if(openResult.charAt(0)==openResult.charAt(1) || openResult.charAt(2)==openResult.charAt(1)
                || openResult.charAt(2)==openResult.charAt(0))
        {
            //不能有相同的
            return 0;
        }

        for (String betNum : betNums) {
            if (resultCodeSum == Integer.parseInt(betNum)) {
                return 1;
            }
        }

        return 0;
    }
}
