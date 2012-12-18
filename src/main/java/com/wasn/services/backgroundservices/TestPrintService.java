package com.wasn.services.backgroundservices;

import android.os.AsyncTask;
import com.wasn.activities.SettingsActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.BluetoothNotAvailableException;
import com.wasn.exceptions.BluetoothNotEnableException;
import com.wasn.exceptions.CannotConnectToPrinterException;
import com.wasn.exceptions.CannotPrintException;
import com.wasn.utils.PrintUtils;

import java.io.IOException;

/**
 * Background task that handles test printing
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class TestPrintService extends AsyncTask<String, String, String> {

    SettingsActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public TestPrintService(SettingsActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        return print(strings[0]);
    }

    /**
     * print test receipt
     * @param printerAddress
     * @return
     */
    public String print(String printerAddress) {
        // send data to printer
        try {
            PrintUtils.printTestPrint(printerAddress);

            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "-1";
        } catch (BluetoothNotEnableException e) {
            e.printStackTrace();
            return "-2";
        } catch (BluetoothNotAvailableException e) {
            e.printStackTrace();
            return "-3";
        } catch (CannotConnectToPrinterException e) {
            e.printStackTrace();
            return "-4";
        } catch (CannotPrintException e) {
            e.printStackTrace();
            return "0";
        } catch (IllegalArgumentException e) {
            // invalid bluetooth address
            return "-5";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostPrint(status);
    }
}
