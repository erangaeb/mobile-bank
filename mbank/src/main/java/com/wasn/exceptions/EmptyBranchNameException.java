package com.wasn.exceptions;

/**
 * Exception that need to throw when branch name is empty
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class EmptyBranchNameException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Empty branch name";
    }
}
