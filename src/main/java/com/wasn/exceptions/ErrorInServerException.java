package com.wasn.exceptions;

/**
 * Exception that need to throw when server error
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class ErrorInServerException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Server error";
    }
}
