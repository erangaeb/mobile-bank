package com.wasn.utils;

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
        if(accountNo.equals("") || amount.equals("")) {
            throw new IllegalArgumentException();
        }

        // check availability of account

        // validate amount
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
}
