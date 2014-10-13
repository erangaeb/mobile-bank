package com.wasn.utils;

import com.wasn.exceptions.EmptyBranchNameException;
import com.wasn.exceptions.EmptyPrinterAddressException;
import com.wasn.exceptions.EmptyTelephoneNoException;
import com.wasn.exceptions.UnTestedPrinterAddressException;

/**
 * Utility class for settings activity
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SettingsUtils {

    /**
     * Validate printer address
     * @param printerAddress printer address
     * @param printerAddressOnDatabase printer address that stored in database
     * @param isTestedAddress tested printer address
     */
    public static void validatePrinterAddress(String printerAddress, String printerAddressOnDatabase, boolean isTestedAddress) throws  UnTestedPrinterAddressException, EmptyPrinterAddressException {
        if (printerAddress.equals("")) {
            // check empty printer address since it doesn't have default value
            throw new EmptyPrinterAddressException();
        } else {
            if(!printerAddress.equals(printerAddressOnDatabase)) {
                // new printer address
                if(!isTestedAddress) {
                    // check weather tested printer address
                    throw new UnTestedPrinterAddressException();
                }
            }
        }
    }

    /**
     * Validate telephone no
     * @param telephoneNo telephone no
     */
    public static void validateTelephoneNo(String telephoneNo) throws EmptyTelephoneNoException {
        if(telephoneNo.equals("")) {
            // empty telephone
            throw new EmptyTelephoneNoException();
        }
    }

    /**
     * Validate branch name
     * @param branchName branch name
     */
    public static void validateBranchName(String branchName) throws EmptyBranchNameException {
        if(branchName.equals("")) {
            // empty branch name
            throw new EmptyBranchNameException();
        }
    }
}
