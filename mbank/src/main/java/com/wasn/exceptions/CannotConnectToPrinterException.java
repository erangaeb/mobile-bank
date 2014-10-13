package com.wasn.exceptions;

/**
 * Exception that need to throw when cannot connect tot printer
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class CannotConnectToPrinterException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Cannot connect to printer";
    }
}
