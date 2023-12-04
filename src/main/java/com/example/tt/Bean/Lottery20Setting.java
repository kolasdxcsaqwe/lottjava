package com.example.tt.Bean;

public class Lottery20Setting {
    private Integer id;

    private Integer roomid;

    private Boolean gameopen;

    private Float da;

    private Float xiao;

    private Float dan;

    private Float shuang;

    private Float anythree;

    private Float anyfour;

    private Float frontfourfix;

    private Float endfourfix;

    private Float frontfourany;

    private Float endfourany;

    private Float minbet;

    private Float maxbet;

    private String rules;

    private Integer fengtime;

    public Integer getFengtime() {
        return fengtime;
    }

    public void setFengtime(Integer fengtime) {
        this.fengtime = fengtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Boolean getGameopen() {
        return gameopen;
    }

    public void setGameopen(Boolean gameopen) {
        this.gameopen = gameopen;
    }

    public Float getDa() {
        return da;
    }

    public void setDa(Float da) {
        this.da = da;
    }

    public Float getXiao() {
        return xiao;
    }

    public void setXiao(Float xiao) {
        this.xiao = xiao;
    }

    public Float getDan() {
        return dan;
    }

    public void setDan(Float dan) {
        this.dan = dan;
    }

    public Float getShuang() {
        return shuang;
    }

    public void setShuang(Float shuang) {
        this.shuang = shuang;
    }

    public Float getAnythree() {
        return anythree;
    }

    public void setAnythree(Float anythree) {
        this.anythree = anythree;
    }

    public Float getAnyfour() {
        return anyfour;
    }

    public void setAnyfour(Float anyfour) {
        this.anyfour = anyfour;
    }

    public Float getFrontfourfix() {
        return frontfourfix;
    }

    public void setFrontfourfix(Float frontfourfix) {
        this.frontfourfix = frontfourfix;
    }

    public Float getEndfourfix() {
        return endfourfix;
    }

    public void setEndfourfix(Float endfourfix) {
        this.endfourfix = endfourfix;
    }

    public Float getFrontfourany() {
        return frontfourany;
    }

    public void setFrontfourany(Float frontfourany) {
        this.frontfourany = frontfourany;
    }

    public Float getEndfourany() {
        return endfourany;
    }

    public void setEndfourany(Float endfourany) {
        this.endfourany = endfourany;
    }

    public Float getMinbet() {
        return minbet;
    }

    public void setMinbet(Float minbet) {
        this.minbet = minbet;
    }

    public Float getMaxbet() {
        return maxbet;
    }

    public void setMaxbet(Float maxbet) {
        this.maxbet = maxbet;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }
}