package com.example.tt.utils;

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

    public static boolean isDigitOnly(String string) {
        if (isNullAmongOf(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            boolean isNumber=index >= 48 && index <= 57;
            boolean isSymbol=index=='-';
            if(isSymbol && i>0)
            {
                return false;
            }

            if (isNumber) {
            } else {
                return false;
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
}
