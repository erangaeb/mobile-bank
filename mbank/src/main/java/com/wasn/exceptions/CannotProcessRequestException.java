package com.wasn.exceptions;

/**
 * Exception that need to throw when status except 201 or 500
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class CannotProcessRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Cannot process request";
    }

}
