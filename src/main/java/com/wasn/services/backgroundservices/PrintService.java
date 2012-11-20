package com.wasn.services.backgroundservices;

import android.content.Intent;
import android.os.AsyncTask;
import com.wasn.activities.MobileBankActivity;
import com.wasn.activities.TransactionDetailsActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Transaction;

/**
 * Background task that handles printing
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class PrintService extends AsyncTask<String, String, String> {

    TransactionDetailsActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public PrintService(TransactionDetailsActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        // print state determine PRINT,RE_PRINT or SUMMARY_PRINT
        String printState = strings[0];

        // send data to printer according to print state
        if(printState.equals("PRINT")) {
            Transaction transaction = application.getTransaction();

            // todo send data to printer

            // after printing save transaction and receipt no update client balance
            application.getMobileBankData().insertTransaction(transaction);
            application.getMobileBankData().setReceiptNo(Integer.toString(transaction.getId()));
            application.getMobileBankData().updateBalanceAmount(transaction.getClientAccountNo(), transaction.getCurrentBalance());
        } else if(printState.equals("RE_PRINT")) {
            // todo send data to printer
        } else {
            // print summary
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostPrint();
    }
}
