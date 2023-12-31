package com.example.tt.Bean;

import java.util.Date;

public class FC3DOrder implements Cloneable{
    private Integer id;

    private String term;

    private Integer money;

    private Date addtime;

    private Integer status;

    private Integer roomid;

    private Float winmoney;

    private String gamename;

    private Integer gametype;

    private Integer orderamount;

    private Float winrate;

    private Integer unitprice;

    private String userid;

    private String username;

    private String headimg;

    private String mingci;

    private String content;

    private String jia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term == null ? null : term.trim();
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Float getWinmoney() {
        return winmoney;
    }

    public void setWinmoney(Float winmoney) {
        this.winmoney = winmoney;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename == null ? null : gamename.trim();
    }

    public Integer getGametype() {
        return gametype;
    }

    public void setGametype(Integer gametype) {
        this.gametype = gametype;
    }

    public Integer getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(Integer orderamount) {
        this.orderamount = orderamount;
    }

    public Float getWinrate() {
        return winrate;
    }

    public void setWinrate(Float winrate) {
        this.winrate = winrate;
    }

    public Integer getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Integer unitprice) {
        this.unitprice = unitprice;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getMingci() {
        return mingci;
    }

    public void setMingci(String mingci) {
        this.mingci = mingci == null ? null : mingci.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getJia() {
        return jia;
    }

    public void setJia(String jia) {
        this.jia = jia == null ? null : jia.trim();
    }

    @Override
    public FC3DOrder clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (FC3DOrder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}