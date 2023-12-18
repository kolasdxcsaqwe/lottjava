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

    public int calNiu()
    {
        return 0;
    }
}
