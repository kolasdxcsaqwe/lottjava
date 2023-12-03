package com.example.tt.utils;

import java.util.Calendar;

public class MyLog {

    public static void e(String content)
    {
        StringBuilder sb=new StringBuilder();
        sb.append(TimeUtils.convertToDetailTime(Calendar.getInstance().getTime()));
        sb.append("  :  ").append(content);
        System.err.println(sb.toString());
    }

    public static void l(String content)
    {
        StringBuilder sb=new StringBuilder();
        sb.append(TimeUtils.convertToDetailTime(Calendar.getInstance().getTime()));
        sb.append("  :  ").append(content);
        System.out.println(sb.toString());
    }
}
