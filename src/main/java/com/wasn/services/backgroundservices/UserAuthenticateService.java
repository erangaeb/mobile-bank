package com.wasn.services.backgroundservices;

import android.os.AsyncTask;
import com.wasn.activities.LoginActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.CannotProcessRequestException;
import com.wasn.exceptions.UnAuthenticatedUserException;
import com.wasn.services.DataCommunication;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Background task that handles user authentication
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class UserAuthenticateService extends AsyncTask<String, String, String> {

    LoginActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public UserAuthenticateService(LoginActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        return authenticate(strings[0], strings[1]);
    }

    /**
     * Authenticate user
     * @param username user name
     * @param password password
     * @return user validity
     */
    private String authenticate(String username, String password) {
        try {
            // get branch id
            // store it in database
            int branchID = new DataCommunication().authenticateUser(username, password);

            // match branch id
            // mismatch means different branch id
            // need to download data
            if(!application.getMobileBankData().getBranchId().equals(Integer.toString(branchID))) {
                application.getMobileBankData().setDownloadState("0");
                application.getMobileBankData().setBranchId(Integer.toString(branchID));
            }
            return "1";
        } catch (IOException e) {
            // cannot process request
            e.printStackTrace();
            return "-1";
        } catch (URISyntaxException e) {
            // cannot process request
            e.printStackTrace();
            return "-1";
        } catch (CannotProcessRequestException e) {
            // cannot process request
            e.printStackTrace();
            return "-1";
        } catch (UnAuthenticatedUserException e) {
            // authentication fail
            e.printStackTrace();
            return "0";
        } catch (NumberFormatException e) {
            // server response error
            e.printStackTrace();
            return "-2";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostLogin(status);
    }
}
