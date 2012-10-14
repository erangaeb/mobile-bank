package com.wasn.pojos;

/**
 * POJO class to hold client details
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class Client {

    String id;
    String name;
    String nic;
    String birthDate;
    String accountNo;
    String balanceAmount;
    String previousTransaction;

    public Client(String id, String name, String nic, String birthDate, String accountNo, String balanceAmount, String previousTransaction) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.birthDate = birthDate;
        this.accountNo = accountNo;
        this.balanceAmount = balanceAmount;
        this.previousTransaction = previousTransaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getPreviousTransaction() {
        return previousTransaction;
    }

    public void setPreviousTransaction(String previousTransaction) {
        this.previousTransaction = previousTransaction;
    }
}
