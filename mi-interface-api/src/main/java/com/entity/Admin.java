package com.entity;

import java.io.Serializable;

public class Admin implements Serializable {

    private static final long serialVersionUID = -6915374959836606572L;
    private Integer aId;

    private String aName;

    private String aPass;

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName == null ? null : aName.trim();
    }

    public String getaPass() {
        return aPass;
    }

    public void setaPass(String aPass) {
        this.aPass = aPass == null ? null : aPass.trim();
    }

    public Admin() {
    }

    public Admin(Integer aId,String aName) {
        this.aId = aId;
        this.aName = aName;
    }
}