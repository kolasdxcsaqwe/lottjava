package com.example.tt.Bean;

import java.util.Date;

public class TopUpLog {
    private Integer id;

    private Integer money;

    private Date time;

    private Integer roomid;

    private String orderid;

    private String tixian;

    private String czfangshi;

    private String userid;

    private String headimg;

    private String username;

    private String type;

    private String status;

    private String game;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getTixian() {
        return tixian;
    }

    public void setTixian(String tixian) {
        this.tixian = tixian == null ? null : tixian.trim();
    }

    public String getCzfangshi() {
        return czfangshi;
    }

    public void setCzfangshi(String czfangshi) {
        this.czfangshi = czfangshi == null ? null : czfangshi.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game == null ? null : game.trim();
    }

    public String getJia() {
        return jia;
    }

    public void setJia(String jia) {
        this.jia = jia == null ? null : jia.trim();
    }
}