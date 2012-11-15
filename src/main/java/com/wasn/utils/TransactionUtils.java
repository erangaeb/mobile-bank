package com.wasn.utils;

import com.wasn.exceptions.InvalidAccountException;
import com.wasn.exceptions.InvalidBalanceAmountException;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

import java.text.DecimalFormat;

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
    public static void validateFields(String accountNo, String amount) throws IllegalArgumentException, NumberFormatException {
        // check empty of fields
        if (accountNo.equals("") || amount.equals("")) {
            throw new IllegalArgumentException();
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
     * @param account account no
     * @param amount  transaction amount
     * @param client  transaction client
     * @return transaction
     * @throws InvalidAccountException
     */
    public static Transaction createTransaction(String branchId, String account, String amount, Client client) throws InvalidAccountException, InvalidBalanceAmountException {
        // can raise InvalidBalanceAmountException
        try {
            // check availability of account and create transaction
            if (account.equals(client.getAccountNo())) {
                Transaction transaction = new Transaction(branchId,
                                                          client.getName(),
                                                          client.getNic(),
                                                          account,
                                                          client.getBalanceAmount(),
                                                          amount,
                                                          getBalanceAmount(client.getBalanceAmount(), amount),
                                                          "transaction-tim",
                                                          "receipt-id",
                                                          client.getId(),
                                                          "DEPOSIT",
                                                          "check-no",
                                                          "description");

                return transaction;
            } else {
                throw new InvalidAccountException();
            }
        } catch(InvalidBalanceAmountException e) {
            throw e;
        }
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

}
