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
            application.getMobileBankData().insertTransaction(transaction);
        } else if(printState.equals("RE_PRINT")) {
            Transaction transaction = application.getTransaction();
            application.getMobileBankData().insertTransaction(transaction);
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

        // download
        //activity.startActivity(new Intent(activity, MobileBankActivity.class));
        activity.finish();
    }
}
