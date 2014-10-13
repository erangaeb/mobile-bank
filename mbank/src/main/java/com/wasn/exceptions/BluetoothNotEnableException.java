package com.wasn.exceptions;

/**
 * Exception that need to throw when bluetooth not enable
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class BluetoothNotEnableException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Bluetooth not enable";
    }
}
