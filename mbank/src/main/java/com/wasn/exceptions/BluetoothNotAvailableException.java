package com.wasn.exceptions;

/**
 * Exception that need to throw when bluetooth not available
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class BluetoothNotAvailableException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Bluetooth not available";
    }
}
