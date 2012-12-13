package com.wasn.pojos;

/**
 * POJO class to hold attributes of transaction summary
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class Summary {
    private String branchId;
    private String transactionCount;
    private String totalTransactionAmount;
    private String time;
    private String lastReceiptId;

    public Summary(String branchId, String transactionCount, String totalTransactionAmount, String time, String lastReceiptId) {
        this.branchId = branchId;
        this.transactionCount = transactionCount;
        this.totalTransactionAmount = totalTransactionAmount;
        this.time = time;
        this.lastReceiptId = lastReceiptId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(String totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public String getLastReceiptId() {
        return lastReceiptId;
    }

    public void setLastReceiptId(String lastReceiptId) {
        this.lastReceiptId = lastReceiptId;
    }
}
