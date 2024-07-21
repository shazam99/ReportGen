package com.natwest.reportGen.model;

public class ReferenceRecord {
    private String refkey1;
    private String refdata1;
    private String refkey2;
    private String refdata2;
    private String refdata3;
    private double refdata4;

    public ReferenceRecord() {
        this.refkey1 = "";
        this.refdata1 = "";
        this.refkey2 = "";
        this.refdata2 = "";
        this.refdata3 = "";
        this.refdata4 = 0.0;
    }

    public ReferenceRecord(String refkey1, String refdata1, String refkey2, String refdata2, String refdata3, double refdata4) {
        this.refkey1 = refkey1;
        this.refdata1 = refdata1;
        this.refkey2 = refkey2;
        this.refdata2 = refdata2;
        this.refdata3 = refdata3;
        this.refdata4 = refdata4;
    }

    public String getRefkey1() {
        return refkey1;
    }

    public void setRefkey1(String refkey1) {
        this.refkey1 = refkey1;
    }

    public String getRefdata1() {
        return refdata1;
    }

    public void setRefdata1(String refdata1) {
        this.refdata1 = refdata1;
    }

    public String getRefkey2() {
        return refkey2;
    }

    public void setRefkey2(String refkey2) {
        this.refkey2 = refkey2;
    }

    public String getRefdata2() {
        return refdata2;
    }

    public void setRefdata2(String refdata2) {
        this.refdata2 = refdata2;
    }

    public String getRefdata3() {
        return refdata3;
    }

    public void setRefdata3(String refdata3) {
        this.refdata3 = refdata3;
    }

    public double getRefdata4() {
        return refdata4;
    }

    public void setRefdata4(double refdata4) {
        this.refdata4 = refdata4;
    }
}