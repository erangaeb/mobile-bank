package com.wasn.pojos;

/**
 * POJO class to hold settings attributes
 */
public class Settings {

    String printerAddress;
    String branchTelephoeNo;
    String branchName;

    public Settings(String printerAddress, String branchTelephoeNo, String branchName) {
        this.printerAddress = printerAddress;
        this.branchTelephoeNo = branchTelephoeNo;
        this.branchName = branchName;
    }

    public String getPrinterAddress() {
        return printerAddress;
    }

    public void setPrinterAddress(String printerAddress) {
        this.printerAddress = printerAddress;
    }

    public String getBranchTelephoeNo() {
        return branchTelephoeNo;
    }

    public void setBranchTelephoeNo(String branchTelephoeNo) {
        this.branchTelephoeNo = branchTelephoeNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
