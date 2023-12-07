package com.example.tt.FormatCheck;

import com.example.tt.utils.JSONArray;

public class QXCBetHandler {

    public static boolean check(JSONArray jsonArray,int type)
    {
        if(jsonArray.length()<1)
        {
            return false;
        }

        switch (type)
        {
            case 1:
                if(jsonArray.optString(0).length()<3)
                {
                    return false;
                }
                break;
            case 2:
                if(jsonArray.optString(0).length()<4)
                {
                    return false;
                }
                break;
            case 3:
            case 4:
                if(jsonArray.optString(0).length()!=4)
                {
                    return false;
                }
                break;
            case 5:
            case 6:
                if(jsonArray.optString(0).length()<1)
                {
                    return false;
                }
                break;
            case 7:
                if(jsonArray.optString(0).length()<2)
                {
                    return false;
                }
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                if(jsonArray.optString(0).length()<1)
                {
                    return false;
                }
                break;

        }

        return true;
    }



}
