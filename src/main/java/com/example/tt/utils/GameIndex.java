package com.example.tt.utils;

public class GameIndex {

    public static Integer getLotteryIndex(String betGame) {
        if (betGame == null) {
            return -1;
        }
        if (betGame.equalsIgnoreCase("pk10")) {
            return 1;
        } else if (betGame.equalsIgnoreCase("xyft")) {
            return 2;
        } else if (betGame.equalsIgnoreCase("cqssc")) {
            return 3;
        } else if (betGame.equalsIgnoreCase("xy28")) {
            return 4;
        } else if (betGame.equalsIgnoreCase("ny28")) {
            return 19;
        } else if (betGame.equalsIgnoreCase("jnd28")) {
            return 5;
        } else if (betGame.equalsIgnoreCase("jsmt")) {
            return 6;
        } else if (betGame.equalsIgnoreCase("jssc")) {
            return 7;
        } else if (betGame.equalsIgnoreCase("jsssc")) {
            return 8;
        } else if (betGame.equalsIgnoreCase("kuai3")) {
            return 9;
        } else if (betGame.equalsIgnoreCase("bjl")) {
            return 10;
        } else if (betGame.equalsIgnoreCase("gd11x5")) {
            return 11;
        } else if (betGame.equalsIgnoreCase("jssm")) {
            return 12;
        } else if (betGame.equalsIgnoreCase("lhc")) {
            return 13;
        } else if (betGame.equalsIgnoreCase("jslhc")) {
            return 14;
        } else if (betGame.equalsIgnoreCase("twk3")) {
            return 15;
        } else if (betGame.equalsIgnoreCase("txffc")) {
            return 16;
        } else if (betGame.equalsIgnoreCase("azxy10")) {
            return 17;
        } else if (betGame.equalsIgnoreCase("azxy5")) {
            return 18;
        }

        return -1;
    }
}
