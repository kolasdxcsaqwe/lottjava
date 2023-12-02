package com.example.tt.Bean;

public class RobotUser {
    private Integer id;

    private String userid;

    private Integer rare;

    private Integer runstatus;

    private String headimg;

    private String name;

    private String plan;

    private String game;

    private String roomid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public Integer getRare() {
        return rare;
    }

    public void setRare(Integer rare) {
        this.rare = rare;
    }

    public Integer getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(Integer runstatus) {
        this.runstatus = runstatus;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan == null ? null : plan.trim();
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game == null ? null : game.trim();
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid == null ? null : roomid.trim();
    }
}