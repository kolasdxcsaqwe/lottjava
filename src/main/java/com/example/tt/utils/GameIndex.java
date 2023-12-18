package com.example.tt.utils;

public class GameIndex {

    public enum OrderCalculateStatus {
        win(1,  "赢了"),
        lost(2,  "输了"),
        quit(9,  "撤单"),
        unCalculate(0,  "未结算");

        private int code;
        private String explain;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        OrderCalculateStatus(int code, String explain) {
            this.code = code;
            this.explain=explain;
        }
    }

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
        qxc(20, "qxc", "传统七星彩"),
        fc3d(21, "fc3d", "福彩3D"),
        pl5(22, "pl5", "排列5");


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
            lotteryTypeCodeList=LotteryTypeCodeList.valueOf(betGame.trim().toLowerCase());
        }
        catch (Exception ex)
        {
           MyLog.e("获取彩种错误,不存在"+betGame);
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
        ry3(1, "ry3", "任选3"),
        ry2(2, "ry2", "任选2"),
        d4(3, "d4", "前4定位"),
        d3(4, "d3", "前3定位"),
        d2(5, "d2", "前2定位"),
        d1(6, "d1", "定位胆"),
        tw(7, "tw", "头尾"),
        dxds(8, "dxds", "大小单双");


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


    public enum PL5GameTypeCode {
        ry3(1, "ry3", "任选3"),
        ry2(2, "ry2", "任选2"),
        d5(3, "d5", "前5定位"),
        d3(4, "d3", "前3定位"),
        d2(5, "d2", "前2定位"),
        d1(6, "d1", "定位胆"),
        dn(7, "dn", "斗牛"),
        dxds(8, "dxds", "大小单双");


        private int code;
        private String game;
        private String explain;

        PL5GameTypeCode(int code, String game, String explain) {
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

        public static PL5GameTypeCode getPL5GameTypeCode(String gameName) {

            PL5GameTypeCode qxcGameTypeCode= null;

            try
            {
                qxcGameTypeCode=PL5GameTypeCode.valueOf(gameName);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return qxcGameTypeCode;
        }
    }
}
