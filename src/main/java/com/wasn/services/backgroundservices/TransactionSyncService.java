package com.wasn.services.backgroundservices;

import android.content.Intent;
import android.os.AsyncTask;
import com.wasn.activities.DownloadActivity;
import com.wasn.activities.MobileBankActivity;
import com.wasn.activities.TransactionListActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Transaction;
import com.wasn.utils.TransactionUtils;

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
        // todo sync transaction

        // todo update database transaction sync state
        application.getMobileBankData().updateTransactionSyncState(unSyncedTransactionList);

        return "0";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostSync();
    }
}
