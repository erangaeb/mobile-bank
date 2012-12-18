package com.wasn.exceptions;

/**
 * Exception that need to throw when no test print printed
 */
public class UnTestedPrinterAddressException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "No printed test print";
    }
}
