package com.wasn.exceptions;

/**
 * Exception that need to throw when printer address is empty
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class EmptyPrinterAddressException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Empty printer address";
    }
}
