package com.example.tt.utils;

public class GameIndex {

    public static final String[] DXDS={"大","小","单","双"};
    public static final String[] douNiuTitles = {"无牛", "牛一", "牛二", "牛三", "牛四", "牛五", "牛六", "牛七", "牛八", "牛九", "牛牛"};

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

    public static String getLotteryGameName(int gameCode) {
        for(LotteryTypeCodeList lotteryTypeCodeList: LotteryTypeCodeList.values())
        {
            if(lotteryTypeCodeList.code==gameCode)
            {
                return lotteryTypeCodeList.game;
            }
        }

        return "";
    }

    public static String getLotteryGameExplain(int gameCode) {
        for(LotteryTypeCodeList lotteryTypeCodeList: LotteryTypeCodeList.values())
        {
            if(lotteryTypeCodeList.code==gameCode)
            {
                return lotteryTypeCodeList.explain;
            }
        }

        return "";
    }

    public enum QXCGameTypeCode {
        ry3(1, "ry3", "任选3",1),
        ry2(2, "ry2", "任选2",1),
        d4(3, "d4", "4星直选",4),
        d3(4, "d3", "3星直选",3),
        d2(5, "d2", "2星直选",2),
        d1(6, "d1", "定位胆",4),
        tw(7, "tw", "头尾",2),
        dxds(8, "dxds", "大小单双",4);


        private int code;
        private String game;
        private String explain;

        private int line;

        QXCGameTypeCode(int code, String game, String explain,int line) {
            this.code = code;
            this.game = game;
            this.explain=explain;
            this.line=line;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
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
        ry3(1, "ry3", "任选3",1),
        ry2(2, "ry2", "任选2",1),
        d5(3, "d5", "5星直选",5),
        d3(4, "d3", "3星直选",3),
        sxzs(9, "sxzs", "3星组三",1),
        sxzl(10, "sxzl", "3星组六",1),
        d2(5, "d2", "2星直选",2),
        d1(6, "d1", "定位胆",5),
        dn(7, "dn", "斗牛",1),
        dxds(8, "dxds", "大小单双",5);



        private int code;
        private String game;
        private String explain;

        private int line;

        PL5GameTypeCode(int code, String game, String explain,int line) {
            this.code = code;
            this.game = game;
            this.explain=explain;
            this.line=line;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
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


    public enum FC3DGameTypeCode {
        ry2(1, "ry2", "任选二",1),
        ry1(2, "ry1", "任选一",1),
        d3(3, "d3", "3星直选",3),
        d3z3(4, "d3z3", "3星组三",1),
        d3z6(5, "d3z6", "3星组六",1),
        d3z3sum(6, "d3z3sum", "3星组三和值",1),
        d3z6sum(7, "d3z6sum", "3星组六和值",1),
        d2f(8, "d2f", "2星前二直选",2),
        d2b(9, "d2b", "2星后二直选",2),
        d1(10, "d1", "定位胆",3),
        dxds(11, "dxds", "大小单双",3);


        private int code;
        private String game;
        private String explain;

        private int line;

        FC3DGameTypeCode(int code, String game, String explain,int line) {
            this.code = code;
            this.game = game;
            this.explain=explain;
            this.line=line;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
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

        public static FC3DGameTypeCode getFC3DGameTypeCode(String gameName) {

            FC3DGameTypeCode qxcGameTypeCode= null;

            try
            {
                qxcGameTypeCode=FC3DGameTypeCode.valueOf(gameName);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return qxcGameTypeCode;
        }
    }
}
