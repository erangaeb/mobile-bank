package com.wasn.exceptions;

/**
 * Exception that need to throw when user authentication fails
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class UnAuthenticatedUserException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "un-authenticated user";
    }
}
