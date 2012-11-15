package com.wasn.exceptions;

/**
 * Exception that need to throw when data lost
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class DataLostException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Data lost while transmitting";
    }

}
