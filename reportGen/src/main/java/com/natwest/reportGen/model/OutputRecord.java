package com.natwest.reportGen.model;

public class OutputRecord {
    private String outfield1;
    private String outfield2;
    private String outfield3;
    private double outfield4;
    private double outfield5;

    public OutputRecord(String outfield1, String outfield2, String outfield3, double outfield4, double outfield5) {
        this.outfield1 = outfield1;
        this.outfield2 = outfield2;
        this.outfield3 = outfield3;
        this.outfield4 = outfield4;
        this.outfield5 = outfield5;
    }

    public String[] toCsvRow() {
        return new String[]{
                outfield1,
                outfield2,
                outfield3,
                String.valueOf(outfield4),
                String.valueOf(outfield5)
        };
    }

    public String getOutfield1() {
        return outfield1;
    }

    public void setOutfield1(String outfield1) {
        this.outfield1 = outfield1;
    }

    public String getOutfield2() {
        return outfield2;
    }

    public void setOutfield2(String outfield2) {
        this.outfield2 = outfield2;
    }

    public String getOutfield3() {
        return outfield3;
    }

    public void setOutfield3(String outfield3) {
        this.outfield3 = outfield3;
    }

    public double getOutfield4() {
        return outfield4;
    }

    public void setOutfield4(double outfield4) {
        this.outfield4 = outfield4;
    }

    public double getOutfield5() {
        return outfield5;
    }

    public void setOutfield5(double outfield5) {
        this.outfield5 = outfield5;
    }

}