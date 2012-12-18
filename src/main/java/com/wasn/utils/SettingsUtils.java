package com.wasn.utils;

import com.wasn.pojos.Settings;

/**
 * Utility class for settings activity
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SettingsUtils {

    /**
     * Validate printer address
     * @param printerAddress
     */
    public static void validatePrinterAddress(String printerAddress) throws IllegalArgumentException {
        // check empty printer address since it dosen't have default value
        if (printerAddress.equals("")) {
            throw new IllegalArgumentException();
        }
    }
}
