package com.wasn.pojos;

/**
 * To hold attributes of transaction
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class Transaction {

    String branchId;
    String clinetName;
    String clinetNic;
    String clientAccountNo;
    String previousBalance;
    String transactionAmount;
    String currentBalance;
    String transactionTime;
    String receiptId;
    String clientId;
    String transactionType;
    String checkNo;
    String description;

    public Transaction(String branchId,
                       String clinetName,
                       String clinetNic,
                       String clientAccountNo,
                       String previousBalance,
                       String transactionAmount,
                       String currentBalance,
                       String transactionTime,
                       String receiptId,
                       String clientId,
                       String transactionType,
                       String checkNo,
                       String description) {
        this.branchId = branchId;
        this.clinetName = clinetName;
        this.clinetNic = clinetNic;
        this.clientAccountNo = clientAccountNo;
        this.previousBalance = previousBalance;
        this.transactionAmount = transactionAmount;
        this.currentBalance = currentBalance;
        this.transactionTime = transactionTime;
        this.receiptId = receiptId;
        this.clientId = clientId;
        this.transactionType = transactionType;
        this.checkNo = checkNo;
        this.description = description;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getClinetName() {
        return clinetName;
    }

    public void setClinetName(String clinetName) {
        this.clinetName = clinetName;
    }

    public String getClinetNic() {
        return clinetNic;
    }

    public void setClinetNic(String clinetNic) {
        this.clinetNic = clinetNic;
    }

    public String getClientAccountNo() {
        return clientAccountNo;
    }

    public void setClientAccountNo(String clientAccountNo) {
        this.clientAccountNo = clientAccountNo;
    }

    public String getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(String previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
