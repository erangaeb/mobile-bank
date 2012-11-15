package com.wasn.application;

import android.app.Application;
import com.wasn.models.MobileBankData;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

/**
 * Application object class of mobile-bank
 * Keep shared objects
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class MobileBankApplication extends Application {

    // currently processing or selected transaction
    Transaction transaction;

    // currently selected client
    Client client;

    // database class instance
    MobileBankData mobileBankData;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        resetFields();

        mobileBankData = new MobileBankData(MobileBankApplication.this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        resetFields();

        // close database connections
        mobileBankData.close();
    }

    /**
     * Reset shared object values
     */
    public void resetFields() {
        transaction = null;
        client = null;
    }

    public MobileBankData getMobileBankData() {
        return mobileBankData;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
