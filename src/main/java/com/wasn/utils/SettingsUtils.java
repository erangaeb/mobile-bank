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
     * @param settings settings attributes
     */
    public static void validatePrinterAddress(Settings settings) throws IllegalArgumentException {
        // check empty printer address since it dosen't have default value
        if (settings.getPrinterAddress().equals("")) {
            throw new IllegalArgumentException();
        }
    }
}
