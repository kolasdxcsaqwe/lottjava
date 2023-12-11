package com.example.tt.Bean;

import java.math.BigDecimal;
import java.util.List;

public class PrizePoolBean implements Cloneable {
    String updateTime;
    int poolStatus;
    BigDecimal totalAmount;
    BigDecimal totalAmountDecimal;
    BigDecimal agqjTotalAmountDecimal;
    BigDecimal aginTotalAmountDecimal;
    BigDecimal totalPool;

    public PrizePoolBean(int poolStatus, BigDecimal totalPool) {
        this.poolStatus = poolStatus;
        this.totalPool = totalPool;
    }

    public BigDecimal getTotalPool() {
        return totalPool;
    }

    public void setTotalPool(BigDecimal totalPool) {
        this.totalPool = totalPool;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(int poolStatus) {
        this.poolStatus = poolStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountDecimal() {
        return totalAmountDecimal;
    }

    public void setTotalAmountDecimal(BigDecimal totalAmountDecimal) {
        this.totalAmountDecimal = totalAmountDecimal;
    }

    public BigDecimal getAgqjTotalAmountDecimal() {
        return agqjTotalAmountDecimal;
    }

    public void setAgqjTotalAmountDecimal(BigDecimal agqjTotalAmountDecimal) {
        this.agqjTotalAmountDecimal = agqjTotalAmountDecimal;
    }

    public BigDecimal getAginTotalAmountDecimal() {
        return aginTotalAmountDecimal;
    }

    public void setAginTotalAmountDecimal(BigDecimal aginTotalAmountDecimal) {
        this.aginTotalAmountDecimal = aginTotalAmountDecimal;
    }

    @Override
    public PrizePoolBean clone() {
        try {
            return (PrizePoolBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
