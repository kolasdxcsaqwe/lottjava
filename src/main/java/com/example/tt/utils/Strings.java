package com.example.tt.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Strings {

    public static boolean isNullAmongOf(String ... args)
    {
        if(args==null)
        {
            return true;
        }
        for (int i = 0; i <args.length ; i++) {
            if(args[i]==null)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmptyOrNullAmongOf(String ... args)
    {
        if(args==null)
        {
            return true;
        }
        for (int i = 0; i <args.length ; i++) {
            if(args[i]==null || args[i].length()==0)
            {
                return true;
            }
        }

        return false;
    }

    public static String makeSpan(String msg,String color,String textSize)
    {
        String temple="<span style='color:%s;font-size:%s;'>%s</span>";
        return String.format(temple,color,textSize,msg);
    }

    public static String makeBoldSpan(String msg,String color,String textSize)
    {
        String temple="<span style='color:%s;font-size:%s;font-weight:bold'>%s</span>";
        return String.format(temple,color,textSize,msg);
    }

    public static boolean isEqualsIgnoreCaseAmongOf(String val,String ... args)
    {
        if(args==null || val==null)
        {
            return false;
        }
        for (int i = 0; i <args.length ; i++) {
            if(args[i]!=null && args[i].length()!=0 && val.equalsIgnoreCase(args[i]))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isDigitOnly(String...string) {
        if (isNullAmongOf(string)) return false;

        for (int q = 0; q < string.length; q++) {
            for (int j = 0; j < string[q].length(); j++) {
                int index = string[q].charAt(j);
                //65 90 A Z
                //97 122 a z
                //48 57 0 9
                boolean isNumber=index >= 48 && index <= 57;
                boolean isSymbol=index=='-';
                if(isSymbol && j>0)
                {
                    return false;
                }

                if (isNumber) {
                } else {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean isDigitWithSymbol(String string) {
        if (isNullAmongOf(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            boolean isNumber=index >= 48 && index <= 57;
            boolean isSymbol=index=='-' || index =='.';
//            if(isSymbol && i>0)
//            {
//                return false;
//            }

            if (isNumber || isSymbol) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean isLetterOnly(String string) {
        if (isNullAmongOf(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            if (index >= 65 && index <= 90) {
            } else if (index >= 97 && index <= 122) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean hasContainsChar(String target, char arg)
    {
        boolean hasInKai=false;
        for (int j = 0; j < target.length(); j++) {
            if(target.charAt(j)==arg)
            {
                hasInKai=true;
            }
        }
        return hasInKai;
    }

    public static boolean hasContainsChar(String target[], char arg)
    {
        boolean hasInKai=false;
        for (int i = 0; i < target.length; i++) {
            if(target[i] == null || target[i].length() != 1)
            {
                return false;
            }
            else
            {
                for (int j = 0; j < target[i].length(); j++) {
                    if(target[i].charAt(j)==arg)
                    {
                        hasInKai=true;
                    }
                }
            }
        }

        return hasInKai;
    }

    //length 代表保留几位
    public static String cutOff(BigDecimal val, int length)
    {
        if(val==null)
        {
            return "";
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("##0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        String format = decimalFormat.format(val);
        return format;
    }

    //length 代表保留几位
    public static String cutOff(String val,int length)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("##0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if(!Strings.isEmptyOrNullAmongOf(val))
        {
            try {
                String format = decimalFormat.format(new BigDecimal(val));
                return format;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return val;
    }

    public static String combineString(String args[])
    {
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]);
        }
        return stringBuilder.toString();
    }
}
