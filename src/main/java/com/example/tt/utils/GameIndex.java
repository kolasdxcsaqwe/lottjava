package com.example.tt.utils;

public class GameIndex {

    public enum LotteryTypeCodeList {
        pk10(1, "pk10", "北京赛车"),
        xyft(2, "xyft", "幸运飞艇"),
        cqssc(3, "xyft", "重庆时时彩"),
        xy28(4, "xy28", "新加坡28"),
        jnd28(5, "jnd28", "加拿大28"),
        jsmt(6, "jsmt", "极速摩托"),
        jssc(7, "jssc", "极速赛车"),
        jsssc(8, "jsssc", "极速时时彩"),
        kuai3(9, "kuai3", "快三"),
        bjl(10, "bjl", "百家乐"),
        gd11x5(11, "gd11x5", "广东11选5"),
        jssm(12, "jssm", "极速赛马"),
        lhc(13, "lhc", "香港六合彩"),
        jslhc(14, "jslhc", "极速六合彩"),
        twk3(15, "twk3", "台湾快三"),
        txffc(16, "txffc", "腾讯分分彩"),
        azxy10(17, "azxy10", "台湾快三"),
        azxy5(18, "azxy5", "台湾快三"),
        ny28(19, "ny28", "纽约28"),
        qxc(20, "qxc", "传统七星彩");


        private int code;
        private String game;
        private String explain;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        LotteryTypeCodeList(int code, String game, String explain) {
            this.code = code;
            this.game = game;
            this.explain=explain;
        }

    }

    public static Integer getLotteryIndex(String betGame) {
        if (betGame == null) {
            return -1;
        }

        LotteryTypeCodeList lotteryTypeCodeList= null;

        try
        {
            lotteryTypeCodeList=LotteryTypeCodeList.valueOf(betGame);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if(lotteryTypeCodeList!=null)
        {
            return lotteryTypeCodeList.code;
        }

        return -1;
    }

    public static String getLotteryGameName(int gameName) {
        for(LotteryTypeCodeList lotteryTypeCodeList: LotteryTypeCodeList.values())
        {
            if(lotteryTypeCodeList.code==gameName)
            {
                return lotteryTypeCodeList.game;
            }
        }

        return "";
    }

    public enum QXCGameTypeCode {
        ry3(1, "ry3", "任意三"),
        ry4(2, "ry4", "任意4"),
        q4dw(3, "q4dw", "前4定位"),
        h4dw(4, "h4dw", "后4定位"),
        q4ry(5, "q4ry", "前4任意"),
        h4ry(6, "h4ry", "后4任意"),
        twdw(7, "twdw", "头尾定位"),
        da(8, "da", "大"),
        xiao(9, "xiao", "小"),
        dan(10, "dan", "单"),
        shuang(11, "shuang", "双");


        private int code;
        private String game;
        private String explain;

        QXCGameTypeCode(int code, String game, String explain) {
            this.code = code;
            this.game = game;
            this.explain=explain;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public static QXCGameTypeCode getQXCGameTypeCode(String gameName) {

            QXCGameTypeCode qxcGameTypeCode= null;

            try
            {
                qxcGameTypeCode=QXCGameTypeCode.valueOf(gameName);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return qxcGameTypeCode;
        }
    }
}
