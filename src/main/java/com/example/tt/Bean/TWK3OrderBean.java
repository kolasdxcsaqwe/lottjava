package com.example.tt.Bean;

import java.util.Date;

public class TWK3OrderBean {
    private Integer id;

    private Integer money;

    private Date addtime;

    private Integer roomid;

    private String chatid;

    private String gamename;

    private Integer gamtype;

    private String userid;

    private String username;

    private String headimg;

    private String term;

    private String mingci;

    private String content;

    private String status;

    private String jia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid == null ? null : chatid.trim();
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename == null ? null : gamename.trim();
    }

    public Integer getGamtype() {
        return gamtype;
    }

    public void setGamtype(Integer gamtype) {
        this.gamtype = gamtype;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term == null ? null : term.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getJia() {
        return jia;
    }

    public void setJia(String jia) {
        this.jia = jia == null ? null : jia.trim();
    }
}