package com.wasn.utils;

import com.wasn.exceptions.EmptyFieldsException;
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
    public static void validateFields(User user) throws EmptyFieldsException {
        // check empty of fields
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            throw new EmptyFieldsException();
        }
    }

    /**
     * Validate administrative password
     * @param password admin password
     * @return validity of password
     */
    public static boolean validateAdminPassword(String password) throws EmptyFieldsException, UnAuthenticatedUserException {
        // check empty
        if(password.equals("")) {
            throw new EmptyFieldsException();
        } else {
            if(password.equals("1234")) {
                return true;
            } else {
                throw new UnAuthenticatedUserException();
            }
        }
    }

}
