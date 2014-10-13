package com.wasn.exceptions;

/**
 * Exception that need to throw when response parsing error
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class ResponseErrorException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "error in server response";
    }

}
