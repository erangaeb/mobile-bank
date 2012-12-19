package com.wasn.utils;

import com.wasn.exceptions.UnAuthenticatedUserException;
import com.wasn.pojos.User;

/**
 * Utility class of login activity
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class LoginUtils {

    /**
     * Validate transaction form fields
     *
     * @param user current user
     */
    public static void validateFields(User user) throws IllegalArgumentException {
        // check empty of fields
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Validate administrative password
     * @param password admin password
     * @return validity of password
     */
    public static boolean validateAdminPassword(String password) throws IllegalArgumentException, UnAuthenticatedUserException {
        // check empty
        if(password.equals("")) {
            throw new IllegalArgumentException();
        } else {
            if(password.equals("1234")) {
                return true;
            } else {
                throw new UnAuthenticatedUserException();
            }
        }
    }

}
