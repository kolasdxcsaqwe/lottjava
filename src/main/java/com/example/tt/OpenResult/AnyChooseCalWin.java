package com.example.tt.OpenResult;

import com.example.tt.utils.Strings;

import java.util.Stack;

public class AnyChooseCalWin {

    static AnyChooseCalWin anyChooseCalWin;
    Stack<Character> stack = new Stack<Character>();
    int win = 0;

    public static AnyChooseCalWin getInstance() {
        if (anyChooseCalWin == null) {
            anyChooseCalWin = new AnyChooseCalWin();
        }
        return anyChooseCalWin;
    }

    public int getWinTimes(String kai,String bet,int anyWhat)
    {
        win=0;
        cal(removeSameNum(kai),bet,anyWhat,0,0);
        return win;
    }

    private String removeSameNum(String num)
    {
        StringBuilder sb=new StringBuilder();
        int pos=0;
        for (int i = 0; i <num.length(); i++) {
            boolean isSame=false;
            for (int j = i+1; j <num.length(); j++) {
                if(num.charAt(i)==num.charAt(j))
                {
                    isSame=true;
                    break;
                }
            }
            if(!isSame)
            {
                sb.append(num.charAt(i));
            }
        }
        return sb.toString();
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
    private void cal(String shu,String bet,int targ, int has, int cur) {
        if(has == targ) {
            System.err.println(stack);
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
