package com.wasn.exceptions;

/**
 * Exception that need to throw when error in synced record
 *
 * eranga.herath@pagero.com (eranga herath)
 */
public class ErrorInSyncedRecordException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Synced record error";
    }
}
