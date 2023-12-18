package com.example.tt.OpenResult;

import com.example.tt.utils.Strings;

import java.util.Map;

public class NiuNiuCalWin {

    static NiuNiuCalWin niuNiuCalWin;

    public static NiuNiuCalWin getInstance() {
        if (niuNiuCalWin == null) {
            niuNiuCalWin = new NiuNiuCalWin();
        }
        return niuNiuCalWin;
    }

    public int calNiu(Map<Integer, String[]> map, Integer index)
    {
        if(map.get(index)==null)
        {
            return 0;
        }

        return map.get(index).length;
    }

    //固定位置的结算 1字定位
    public int calNiu(String openResult, Map<Integer,String[]> map,Integer...positions)
    {
        int winTimes=0;
        for (int i = 0; i < positions.length; i++) {
            String codes[]=map.get(positions[i]);
            if(!Strings.isEmptyOrNullAmongOf(codes))
            {
                boolean hasChar= Strings.hasContainsChar(codes,openResult.charAt(positions[i]));
                if(hasChar)
                {
                    winTimes++;
                }
            }
        }

        return winTimes;
    }


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
}
