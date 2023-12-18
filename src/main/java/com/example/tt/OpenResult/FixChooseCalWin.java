package com.example.tt.OpenResult;

import com.example.tt.utils.Strings;

import java.util.List;
import java.util.Map;

public class FixChooseCalWin {

    static FixChooseCalWin fixChooseCalWin;

    public static boolean checkFormatFixPosition(Map<Integer, String> map, Integer... index) {
        for (Integer integer : index) {
            if (Strings.isEmptyOrNullAmongOf(map.get(integer))) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkFormatAnyPosition(Map<Integer, String> map, Integer... index) {
        for (Integer integer : index) {
            if (!Strings.isEmptyOrNullAmongOf(map.get(integer))) {
                return true;
            }
        }
        return false;
    }

    public static FixChooseCalWin getInstance() {
        if (fixChooseCalWin == null) {
            fixChooseCalWin = new FixChooseCalWin();
        }
        return fixChooseCalWin;
    }

    //固定位置的结算
    public int calFixIsWin(String openResult, Map<Integer,String> map,Integer...positions)
    {
        for (int i = 0; i < positions.length; i++) {
            String code=map.get(positions[i]);
            if(Strings.isEmptyOrNullAmongOf(code))
            {
               return 0;
            }
            else
            {
                boolean hasChar= Strings.hasContainsChar(code,openResult.charAt(positions[i]));
                if(!hasChar)
                {
                    return 0;
                }
            }
        }

        return 1;
    }

    //固定位置的结算 1字定位
    public int calFixOneIsWin(String openResult, Map<Integer,String> map,Integer...positions)
    {
        int winTimes=0;
        for (int i = 0; i < positions.length; i++) {
            String code=map.get(positions[i]);
            if(!Strings.isEmptyOrNullAmongOf(code))
            {
                boolean hasChar= Strings.hasContainsChar(code,openResult.charAt(positions[i]));
                if(hasChar)
                {
                    winTimes++;
                }
            }
        }

        return winTimes;
    }

    //大小单双结算 0123
    public int calDXDS(String openResult, Map<Integer,String> map,Integer...positions)
    {
        int win=0;
        for (int i = 0; i < positions.length; i++) {
            String code=map.get(positions[i]);
            if(Strings.isEmptyOrNullAmongOf(code))
            {
                return 0;
            }
            else
            {
                int result=openResult.charAt(positions[i])-'0';
                int bet=code.charAt(positions[i])-'0';
                switch (bet)
                {
                    case 0:
                        if(result>4)
                        {
                            win++;
                        }
                        break;
                    case 1:
                        if(result<5)
                        {
                            win++;
                        }
                        break;
                    case 2:
                        if(result%2!=0)
                        {
                            win++;
                        }
                        break;
                    case 3:
                        if(result%2==0)
                        {
                            win++;
                        }
                        break;
                }
            }
        }

        return win;
    }


}
