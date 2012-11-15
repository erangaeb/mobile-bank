package com.wasn.services.backgroundservices;

import android.os.AsyncTask;
import com.wasn.activities.MobileBankActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.CannotProcessRequestException;
import com.wasn.exceptions.DataLostException;
import com.wasn.exceptions.ResponseErrorException;
import com.wasn.pojos.Client;
import com.wasn.services.DataCommunication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Background task that handles client data downloading
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class ClientDataDownloadService extends AsyncTask<String, String, String> {

    MobileBankActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public ClientDataDownloadService(MobileBankActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        String branchId = strings[0];

        // download data
        return download(branchId);
    }

    /**
     * Download client data from server
     * @param branchId branch id
     * @return download status
     */
    public String download(String branchId) {
        String downloadStatus = "0";

        // download and save client list
        try {
            ArrayList<Client> clientList = new DataCommunication().getClients(branchId);
            application.getMobileBankData().insetClients(clientList);

            downloadStatus = "1";
        } catch (URISyntaxException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (IOException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (CannotProcessRequestException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (DataLostException e) {
            // data lost while downloading
            e.printStackTrace();
            downloadStatus = "-2";
        } catch (ResponseErrorException e) {
            // server response error
            e.printStackTrace();
            downloadStatus = "-3";
        } catch (android.database.SQLException e) {
            // database error
            e.printStackTrace();
            downloadStatus = "-4";
        }

        return downloadStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);


    }
}
