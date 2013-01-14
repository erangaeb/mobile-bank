package com.wasn.services.backgroundservices;

import android.content.Intent;
import android.os.AsyncTask;
import com.wasn.activities.DownloadActivity;
import com.wasn.activities.MobileBankActivity;
import com.wasn.activities.TransactionListActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.CannotProcessRequestException;
import com.wasn.exceptions.ErrorInServerException;
import com.wasn.exceptions.ErrorInSyncedRecordException;
import com.wasn.pojos.Transaction;
import com.wasn.services.DataCommunication;
import com.wasn.utils.TransactionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Background task that handles transaction sync
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class TransactionSyncService extends AsyncTask<String, String, String> {

    TransactionListActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public TransactionSyncService(TransactionListActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        return sync();
    }

    /**
     * Sync to bank server
     * @return sync status
     */
    public String sync() {
        ArrayList<Transaction> unSyncedTransactionList = TransactionUtils.getUnSyncedTransactionList(application.getTransactionList());

        // sync
        // update database transaction sync state
        try {
            int syncedRecordCount = new DataCommunication().syncTransactions(unSyncedTransactionList);
            application.getMobileBankData().updateTransactionSyncState(unSyncedTransactionList);

            return Integer.toString(syncedRecordCount);
        } catch (URISyntaxException e) {
            // cannot process request
            e.printStackTrace();
            return "0";
        } catch (IOException e) {
            // cannot process request
            e.printStackTrace();
            return "0";
        } catch (CannotProcessRequestException e) {
            // cannot process request
            e.printStackTrace();
            return "0";
        } catch (ErrorInSyncedRecordException e) {
            // synced record error
            e.printStackTrace();
            return "-1";
        } catch (ErrorInServerException e) {
            // server error
            e.printStackTrace();
            return "-2";
        } catch (NumberFormatException e) {
            // server response error
            e.printStackTrace();
            return "-3";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostSync(Integer.parseInt(status));
    }
}
