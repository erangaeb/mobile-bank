package com.wasn.utils;

import com.wasn.exceptions.EmptyFieldsException;
import com.wasn.exceptions.InvalidAccountException;
import com.wasn.exceptions.InvalidBalanceAmountException;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Client;
import com.wasn.pojos.Summary;
import com.wasn.pojos.Transaction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utility class of transaction activity
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionUtils {

    /**
     * Validate transaction form fields
     *
     * @param accountNo
     * @param amount
     */
    public static void validateFields(String accountNo, String amount) throws EmptyFieldsException, NumberFormatException {
        // check empty of fields
        if (accountNo.equals("") || amount.equals("")) {
            throw new EmptyFieldsException();
        }

        // validate amount
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Crete new transaction
     *
     * @param branchId user's branch id
     * @param transactionId current receipt no equals to transaction id
     * @param amount  transaction amount
     * @param client  transaction client
     * @return transaction
     * @throws InvalidAccountException
     */
    public static Transaction createTransaction(String branchId, int transactionId, String amount, Client client) throws InvalidAccountException, InvalidBalanceAmountException {
        Transaction transaction = new Transaction(transactionId,
                                                  branchId,
                                                  client.getName(),
                                                  client.getNic(),
                                                  client.getAccountNo(),
                                                  client.getBalanceAmount(),
                                                  amount,
                                                  getBalanceAmount(client.getBalanceAmount(), amount),
                                                  getCurrentTime(),
                                                  getReceiptId(branchId, transactionId),
                                                  client.getId(),
                                                  "DEPOSIT",
                                                  "check-no",
                                                  "description",
                                                  "0");

        return transaction;
    }

    /**
     * Calculate current balance
     *
     * @param previousBalance
     * @param transactionAmount
     * @return currentBalance
     */
    private static String getBalanceAmount(String previousBalance, String transactionAmount) throws InvalidBalanceAmountException {
        // calculate and format balance into #.## format
        // cna raise number format exception when parsing client balance
        try {
            double balance = (Double.parseDouble(previousBalance)) + (Double.parseDouble(transactionAmount));
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            return decimalFormat.format(balance);
        } catch (NumberFormatException e) {
            throw new InvalidBalanceAmountException();
        }
    }

    /**
     * Get current date and time as transaction time
     * format - yyyy/MM/dd HH:mm:ss
     * @return
     */
    private static String getCurrentTime() {
        //date format
        String DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss";

        // generate time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Generate receipt id according to receipt no and branch id
     * @param branchId  users branch id
     * @param receiptNo receipt no
     * @return receiptId
     */
    public static String getReceiptId(String branchId, int receiptNo) {
        String receiptId;

        if(branchId.length()==1){
            receiptId="0"+branchId + receiptNo;
        } else {
            receiptId=branchId+receiptNo;
        }

        return receiptId;
    }

    /**
     * Filter un synced transactions
     * @param transactionList all transactions
     * @return unSyncedTransactionList
     */
    public static ArrayList<Transaction> getUnSyncedTransactionList(ArrayList<Transaction> transactionList) {
        ArrayList<Transaction> unSyncedTransactionList = new ArrayList<Transaction>();

        // filter un synced transactions
        for (int i=0; i<transactionList.size(); i++) {
            if(transactionList.get(i).getSyncedState().equals("0")) {
                unSyncedTransactionList.add(transactionList.get(i));
            }
        }

        return unSyncedTransactionList;
    }

    /**
     * Get summary as a list of attributes
     * @param transactionList
     */
    public static Summary getSummary(ArrayList<Transaction> transactionList) {
        String branchId = transactionList.get(0).getBranchId();
        int transactionCount = transactionList.size();

        // get formatted total transaction amount
        String totalTransactionAmount = "0.00";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        try {
            totalTransactionAmount = decimalFormat.format(getTotalTransactionAmount(transactionList));
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        String currentTime = getCurrentTime();
        String lastReceiptId = transactionList.get(transactionCount-1).getReceiptId();

        // new summary
        return new Summary(branchId, Integer.toString(transactionCount), totalTransactionAmount, currentTime, lastReceiptId);
    }

    /**
     * Get to total transaction amount
     * @param transactionList
     * @return deposit count
     */
    public static double getTotalTransactionAmount(ArrayList<Transaction> transactionList) throws NumberFormatException {
        double total = 0;

        for(int i=0; i<transactionList.size(); i++) {
            total = total + Double.parseDouble(transactionList.get(i).getTransactionAmount());
        }

        return total;
    }


}
