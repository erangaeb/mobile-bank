package com.wasn.exceptions;

/**
 * Exception that need to throw when cannot print receipt
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class CannotPrintException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Cannot print receipt";
    }
}
