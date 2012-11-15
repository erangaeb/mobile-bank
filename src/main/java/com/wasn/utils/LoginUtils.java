package com.wasn.utils;

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
    public static void validateFields(User user) throws IllegalArgumentException, NumberFormatException {
        // check empty of fields
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            throw new IllegalArgumentException();
        }
    }

}
