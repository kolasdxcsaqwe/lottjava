package com.example.tt.Bean;

import java.util.Date;

public class LotteryOpenBean {
    private Integer id;

    private Integer type;

    private String term;

    private String nextTerm;

    private Date time;

    private Date nextTime;

    private String roomid;

    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term == null ? null : term.trim();
    }

    public String getNextTerm() {
        return nextTerm;
    }

    public void setNextTerm(String nextTerm) {
        this.nextTerm = nextTerm == null ? null : nextTerm.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid == null ? null : roomid.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}