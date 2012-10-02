package com.wasn.exceptions;

/**
 * Exception when throw invalid account
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class InvalidAccountException extends Exception{

    @Override
    public String toString() {
        return "invalid account";
    }

}
