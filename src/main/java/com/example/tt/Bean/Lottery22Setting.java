package com.example.tt.Bean;

public class Lottery22Setting {
    private Integer id;

    private Integer roomid;

    private Boolean gameopen;

    private Float dxds;

    private Float threefix;

    private Float twofix;

    private Float fivefix;

    private Float anythree;

    private Float anytwo;

    private Float douniu;

    private Float onefix;

    private Float minbet;

    private Float maxbet;

    private Integer fengtime;

    private String rules;

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

    public Float getDxds() {
        return dxds;
    }

    public void setDxds(Float dxds) {
        this.dxds = dxds;
    }

    public Float getThreefix() {
        return threefix;
    }

    public void setThreefix(Float threefix) {
        this.threefix = threefix;
    }

    public Float getTwofix() {
        return twofix;
    }

    public void setTwofix(Float twofix) {
        this.twofix = twofix;
    }

    public Float getFivefix() {
        return fivefix;
    }

    public void setFivefix(Float fivefix) {
        this.fivefix = fivefix;
    }

    public Float getAnythree() {
        return anythree;
    }

    public void setAnythree(Float anythree) {
        this.anythree = anythree;
    }

    public Float getAnytwo() {
        return anytwo;
    }

    public void setAnytwo(Float anytwo) {
        this.anytwo = anytwo;
    }

    public Float getDouniu() {
        return douniu;
    }

    public void setDouniu(Float douniu) {
        this.douniu = douniu;
    }

    public Float getOnefix() {
        return onefix;
    }

    public void setOnefix(Float onefix) {
        this.onefix = onefix;
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

    public Integer getFengtime() {
        return fengtime;
    }

    public void setFengtime(Integer fengtime) {
        this.fengtime = fengtime;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }
}