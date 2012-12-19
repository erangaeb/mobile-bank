package com.wasn.exceptions;

/**
 * Exception that need to throw when form fields are empty
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class EmptyFieldsException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Empty fields";
    }
}
