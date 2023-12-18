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
    public int calNiu(String openResult, Map<Integer,String[]> map)
    {
        int niu=syncBull(openResult);

        String[] codes=map.get(0);
        for (int i = 0; i < codes.length; i++) {
            if(Strings.isDigitOnly(codes[i]))
            {
                if(Integer.parseInt(codes[i])==niu)
                {
                    return 1;
                }
            }
        }

        return 0;
    }


    public static int syncBull(String codes) {

        Integer[] cards=new Integer[codes.length()];
        for (int i = 0; i < codes.length(); i++) {
            cards[i]=codes.charAt(i)-'0';
        }

        // 计算总点数
        int sums = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] > 10) {
                cards[i] = 10;
            }
            sums += cards[i];
        }
        //牛牛
        if (sums % 10 == 0) {
            return 10;
        }
        // 牛丁 ~ 牛九
        int bull = 0;
        for (int i = 0; i < cards.length - 2; i++) {
            int cardI = cards[i];
            for (int j = i + 1; j < cards.length; j++) {
                int cardJ = cards[j];
                for (int k = j + 1; k < cards.length; k++) {
                    int cardK = cards[k];
                    int total = cardI + cardJ + cardK;
                    if (total % 10 == 0) {
                        int n = (sums - total) % 10;
                        bull = bull < n ? n : bull;
                    }
                }
            }
        }
        return bull;
    }
}
