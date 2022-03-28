package com.entity;

import java.io.Serializable;
import java.util.Objects;

public class Condition implements Serializable {

    private static final long serialVersionUID = -1299839728133230860L;
    private Integer typeId;
    private Integer lowPrice;
    private Integer highPrice;
    private String pName;
    private Integer pageNum = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition)) return false;
        Condition condition = (Condition) o;
        return Objects.equals(getTypeId(), condition.getTypeId()) &&
                Objects.equals(getLowPrice(), condition.getLowPrice()) &&
                Objects.equals(getHighPrice(), condition.getHighPrice()) &&
                Objects.equals(getpName(), condition.getpName()) &&
                Objects.equals(getPageNum(), condition.getPageNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTypeId(), getLowPrice(), getHighPrice(), getpName(), getPageNum());
    }

    @Override
    public String toString() {
        return "Condition{" +
                "typeId=" + typeId +
                ", lowPrice=" + lowPrice +
                ", highPrice=" + highPrice +
                ", pName='" + pName + '\'' +
                ", pageNum=" + pageNum +
                '}';
    }

    public Condition(Integer typeId, Integer lowPrice, Integer highPrice, String pName, Integer pageNum) {
        this.typeId = typeId;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.pName = pName;
        this.pageNum = pageNum;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Integer getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Integer highPrice) {
        this.highPrice = highPrice;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Condition() {
    }

}
