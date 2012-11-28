package com.wasn.services.backgroundservices;

import android.os.AsyncTask;
import com.wasn.activities.SummaryDetailsActivity;
import com.wasn.activities.TransactionDetailsActivity;
import com.wasn.application.MobileBankApplication;

/**
 * Background task that handles summary printing
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class SummaryPrintService extends AsyncTask<String, String, String> {

    SummaryDetailsActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public SummaryPrintService(SummaryDetailsActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        // todo send data to printer

        application.getMobileBankData().deleteAllTransaction();
        application.resetFields();
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
