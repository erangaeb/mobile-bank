package com.wasn.application;

import android.app.Application;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //resetFields();
    }

    /**
     * Reset shared object values
     */
    public void resetFields() {
        transaction = new Transaction("", "", "", "", "", "", "", "", "", "", "", "", "");
        client = new Client("", "", "", "", "", "", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        //resetFields();
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
