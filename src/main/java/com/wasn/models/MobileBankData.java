package com.wasn.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.wasn.exceptions.InvalidAccountException;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

import java.util.ArrayList;

/**
 * Database class of mobile bank
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class MobileBankData {

    Context context;

    // DBHelper object,
    // DBHelper using only in this class
    DBHelper dbHelper;

    /**
     * Initialize global fields and DBHelper
     */
    public MobileBankData(Context context) {
        this.context=context;
        dbHelper=new DBHelper();
    }

    /**
     * Clean all
     */
    public void close() {
        //close DB helper
        dbHelper.close();
    }

    /**
     * helper class for create,open and upgrade database
     */
    private class DBHelper extends SQLiteOpenHelper {

        // constants
        public static final String DB_NAME = "mobile_bank_db";
        public static final String TABLE_NAME_CLIENT = "client";
        public static final String TABLE_NAME_TRANSACTION = "raw_transaction";
        public static final String TABLE_NAME_APP_DATA = "app_data";

        public static final int DB_VERSION = 4;

        public DBHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table for client
            String sqlClient = "create table client (id TEXT, " +
                                                    "name TEXT, " +
                                                    "nic TEXT, " +
                                                    "birth_date TEXT, " +
                                                    "account_no TEXT PRIMARY KEY, " +
                                                    "balance_amount TEXT, " +
                                                    "previous_transaction TEXT)";
            db.execSQL(sqlClient);

            // create table for transaction
            String sqlTransaction = "create table raw_transaction (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                  "branch_id TEXT, " +
                                                                  "client_id TEXT, " +
                                                                  "client_name TEXT, " +
                                                                  "client_nic TEXT, " +
                                                                  "account_no TEXT, " +
                                                                  "previous_balance TEXT, " +
                                                                  "transaction_amount TEXT, " +
                                                                  "current_balance TEXT, " +
                                                                  "transaction_time TEXT, " +
                                                                  "transaction_type TEXT, " +
                                                                  "check_no TEXT, " +
                                                                  "description TEXT, " +
                                                                  "receipt_id TEXT UNIQUE, " +
                                                                  "synced_state TEXT)";
            db.execSQL(sqlTransaction);

            //create table for store application attributes
            String sqlApplicationData = "create table app_data (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                             "attribute_name TEXT, " +
                                                             "attribute_value TEXT )";
            db.execSQL(sqlApplicationData);

            // add initial application data to DB
            // keep track with user login state
            ContentValues loginValues =new ContentValues();
            loginValues.put("attribute_name", "isLogged");
            loginValues.put("attribute_value", "0");

            // keep track with client data download state
            ContentValues downloadValues =new ContentValues();
            downloadValues.put("attribute_name", "isDownloaded");
            downloadValues.put("attribute_value", "0");

            // user branch id
            ContentValues branchValues=new ContentValues();
            branchValues.put("attribute_name", "branchId");
            branchValues.put("attribute_value", "0");

            // printing receipt no
            ContentValues receiptValues=new ContentValues();
            receiptValues.put("attribute_name", "receiptNo");
            receiptValues.put("attribute_value", "0");

            //insert application data to app_data table
            try {
                //insert fail throw exception
                db.insertOrThrow(DBHelper.TABLE_NAME_APP_DATA, null, loginValues);
                db.insertOrThrow(DBHelper.TABLE_NAME_APP_DATA, null, downloadValues);
                db.insertOrThrow(DBHelper.TABLE_NAME_APP_DATA, null, branchValues);
                db.insertOrThrow(DBHelper.TABLE_NAME_APP_DATA, null, receiptValues);
            } catch (Exception e) {
                System.out.println("database onCreateFails " + e);
            }
        }

        /**
         * {@inheritDoc}
         * call when upgrade the database, normally changing the DB_VERSION
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //drop tables
            db.execSQL("drop table if exists "+ TABLE_NAME_CLIENT);
            db.execSQL("drop table if exists "+ TABLE_NAME_TRANSACTION);
            db.execSQL("drop table if exists "+ TABLE_NAME_APP_DATA);

            //create databases again
            this.onCreate(db);
        }
    }

    /**
     * get isLogged attribute from app_data table
     * @return loginState - login state with server
     */
    public String getLoginState() {
        String loginState="0";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create cursor for get app data   ( table name		     columns   		select		 	 	select args  		 groupby   having  orderby )
        Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APP_DATA , null  , "attribute_name=?", new String[]{"isLogged"},  null   , null ,  null   );

        //has elements in cursor
        if(appDataCursor.moveToNext()) {
            appDataCursor.moveToLast();
            loginState = appDataCursor.getString(2);
        }

        appDataCursor.close();
        db.close();

        return loginState;
    }

    /**
     * update isLogged attribute in app_data
     * @param loginState determine logged in or not (1 or 0)
     */
    public void setLoginState(String loginState) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues appDataValues =new ContentValues();
        appDataValues.put("attribute_value", loginState);

        //update application data
        db.update(DBHelper.TABLE_NAME_APP_DATA, appDataValues, "attribute_name=?",new String[]{"isLogged"});

        db.close();
    }

    /**
     * get data download state attribute from app_data table
     * @return downloadState login state with server
     */
    public String getDownloadState() {
        String downloadState = "0";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create cursor for get app data   ( table name		     columns   		select		 	 	select args  		    groupby   having  orderby )
        Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APP_DATA , null  , "attribute_name=?", new String[]{"isDownloaded"},  null   , null ,  null   );

        //has elements in cursor
        if(appDataCursor.moveToNext()) {
            appDataCursor.moveToLast();
            downloadState = appDataCursor.getString(2);
        }

        appDataCursor.close();
        db.close();

        return downloadState;
    }

    /**
     * update data download state attribute in app_data
     * @param downloadState determine data downloaded in or not (1 or 0)
     */
    public void setDownloadState(String downloadState) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues appDataValues =new ContentValues();
        appDataValues.put("attribute_value", downloadState);

        //update application data
        db.update(DBHelper.TABLE_NAME_APP_DATA, appDataValues, "attribute_name=?",new String[]{"isDownloaded"});

        db.close();
    }

    /**
     * get branch id attribute from app_data table
     * @return branch id
     */
    public String getBranchId() {
        String branchId = "0";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )
        Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APP_DATA , null  , "attribute_name=?", new String[]{"branchId"},  null   , null ,  null   );

        //has elements in cursor
        if(appDataCursor.moveToNext()){
            appDataCursor.moveToLast();
            branchId=appDataCursor.getString(2);
        }

        appDataCursor.close();
        db.close();

        return branchId;
    }

    /**
     * update branchId attribute in app_date table
     * @param branchId branch id
     */
    public void setBranchId(String branchId) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues appDataValues =new ContentValues();
        appDataValues.put("attribute_value", branchId);

        //update application data
        db.update(DBHelper.TABLE_NAME_APP_DATA, appDataValues, "attribute_name=?",new String[]{"branchId"});

        db.close();
    }

    /**
     * get receiptNo attribute from app_data table
     * @return last receipt no
     */
    public String getReceiptNo() {
        String receiptNo = "0";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )
        Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APP_DATA , null  , "attribute_name=?", new String[]{"receiptNo"},  null   , null ,  null   );

        //has elements in cursor
        if(appDataCursor.moveToNext()){
            appDataCursor.moveToLast();
            receiptNo=appDataCursor.getString(2);
        }

        appDataCursor.close();
        db.close();

        return receiptNo;
    }

    /**
     * update receiptNo attribute in app_data table
     * @param receiptNo
     */
    public void setReceiptNo(String receiptNo) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues appDataValues =new ContentValues();
        appDataValues.put("attribute_value", receiptNo);

        //update application data
        db.update(DBHelper.TABLE_NAME_APP_DATA, appDataValues, "attribute_name=?",new String[]{"receiptNo"});

        db.close();
    }

    /**
     * get all client data from database
     * @return clientList
     */
    public ArrayList<Client> getAllClients() {
        ArrayList<Client> clientList = new ArrayList<Client>();

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        // create a cursor to get all client data
        Cursor clientCursor = db.query(DBHelper.TABLE_NAME_CLIENT, null, null, null,null, null, null);

        //until has elements
        while(clientCursor.moveToNext()){
            // get client attributes
            String clientId=clientCursor.getString(0);
            String clientName=clientCursor.getString(1);
            String clientNic=clientCursor.getString(2);
            String birthDate=clientCursor.getString(3);
            String accountNo=clientCursor.getString(4);
            String balanceAmount=clientCursor.getString(5);
            String previousTransaction=clientCursor.getString(6);

            // create client
            Client client=new Client(clientId, clientName, clientNic, birthDate, accountNo, balanceAmount, previousTransaction);

            clientList.add(client);
        }

        clientCursor.close();
        db.close();

        return clientList;
    }

    /**
     * Insert client details in to database
     * @param clients list of client
     */
    public void insetClients(ArrayList<Client> clients) throws android.database.SQLException {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //delete all records in client table
        db.delete(DBHelper.TABLE_NAME_CLIENT, null, null);

        //create content values for client record
        ContentValues clientValues =new ContentValues();

        for(int i=0;i<clients.size();i++){
            //fill content value
            clientValues.put("id", clients.get(i).getId());
            clientValues.put("name", clients.get(i).getName());
            clientValues.put("nic", clients.get(i).getNic());
            clientValues.put("birth_date", clients.get(i).getBirthDate());
            clientValues.put("account_no", clients.get(i).getAccountNo());
            clientValues.put("balance_amount", clients.get(i).getBalanceAmount());
            clientValues.put("previous_transaction", clients.get(i).getPreviousTransaction());

            // throw exception if insert fail
            db.insertOrThrow(DBHelper.TABLE_NAME_CLIENT, null, clientValues);
        }

        db.close();
    }

    /**
     * Get matching client to given account no
     * @param accountNo client's account no
     * @return matching client
     * @throws InvalidAccountException, no matching client means invalid account
     */
    public Client getClient(String accountNo) throws InvalidAccountException {
        Client client = null;

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor clientCursor = db.query(DBHelper.TABLE_NAME_CLIENT, null, "account_no=?" ,new String[]{accountNo},null, null, null);

        // has elements in cursor
        if(clientCursor.moveToNext()) {
            clientCursor.moveToFirst();

            // get client attributes
            String clientId=clientCursor.getString(0);
            String clientName=clientCursor.getString(1);
            String clientNic=clientCursor.getString(2);
            String clientBirthDate=clientCursor.getString(3);
            String balanceAmount=clientCursor.getString(5);
            String previousTransaction=clientCursor.getString(6);

            client = new Client(clientId, clientName, clientNic, clientBirthDate, accountNo, balanceAmount, previousTransaction);
        }

        clientCursor.close();
        db.close();

        // no such client means invalid account
        if(client == null) {
            throw new InvalidAccountException();
        }

        return client;
    }

    /**
     * update account balance of corresponding client
     * @param accountNo clients account no
     * @param balance balance amount
     */
    public void updateBalanceAmount(String accountNo, String balance) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues clientValues =new ContentValues();
        clientValues.put("balance_amount", balance);

        //update balance amount data
        db.update(DBHelper.TABLE_NAME_CLIENT, clientValues, "account_no=?",new String[]{accountNo});

        db.close();
    }

    /**
     * Get all transactions from database
     */
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        // create a cursor to get transaction data
        Cursor transactionCursor = db.query(DBHelper.TABLE_NAME_TRANSACTION, null, null, null,null, null, null);

        // read all
        while(transactionCursor.moveToNext()) {
            // get transaction attributes
            int id = transactionCursor.getInt(0);
            String branchId = transactionCursor.getString(1);
            String clientId = transactionCursor.getString(2);
            String clientName = transactionCursor.getString(3);
            String clientNic = transactionCursor.getString(4);
            String accountNo = transactionCursor.getString(5);
            String previousBalance = transactionCursor.getString(6);
            String transactionAmount = transactionCursor.getString(7);
            String currentBalance = transactionCursor.getString(8);
            String transactionTime = transactionCursor.getString(9);
            String transactionType = transactionCursor.getString(10);
            String checkNo = transactionCursor.getString(11);
            String description = transactionCursor.getString(12);
            String receiptId = transactionCursor.getString(13);
            String syncedState = transactionCursor.getString(14);

            Transaction transaction=new Transaction(id,
                                                    branchId,
                                                    clientName,
                                                    clientNic,
                                                    accountNo,
                                                    previousBalance,
                                                    transactionAmount,
                                                    currentBalance,
                                                    transactionTime,
                                                    receiptId,
                                                    clientId,
                                                    transactionType,
                                                    checkNo,
                                                    description,
                                                    syncedState);

            transactionList.add(transaction);
        }

        transactionCursor.close();
        db.close();

        return transactionList;
    }

    /**
     * Insert transaction into database
     * @param transaction
     * @throws SQLiteException
     */
    public void insertTransaction(Transaction transaction) throws android.database.SQLException {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values for transaction record
        ContentValues transactionValues =new ContentValues();
        transactionValues.put("branch_id", transaction.getBranchId());
        transactionValues.put("client_id", transaction.getClientId());
        transactionValues.put("client_name", transaction.getClinetName());
        transactionValues.put("client_nic", transaction.getClinetNic());
        transactionValues.put("account_no", transaction.getClientAccountNo());
        transactionValues.put("previous_balance", transaction.getPreviousBalance());
        transactionValues.put("transaction_amount", transaction.getTransactionAmount());
        transactionValues.put("current_balance", transaction.getCurrentBalance());
        transactionValues.put("transaction_time", transaction.getTransactionTime());
        transactionValues.put("transaction_type", transaction.getTransactionType());
        transactionValues.put("check_no", transaction.getCheckNo());
        transactionValues.put("description", transaction.getDescription());
        transactionValues.put("receipt_id", transaction.getReceiptId());
        transactionValues.put("synced_state", "0");

        // throws exception if inset fails
        db.insertOrThrow(DBHelper.TABLE_NAME_TRANSACTION, null, transactionValues);

        db.close();
    }

    /**
     * update synced state of a transaction
     * set synced_state to 1
     */
    public void updateTransactionSyncState(ArrayList<Transaction> unSyncedTransactionList) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //create content values
        ContentValues updateValues =new ContentValues();
        updateValues.put("synced_state", "1");

        for(int i=0; i<unSyncedTransactionList.size(); i++) {
            //update sync state
            db.update(DBHelper.TABLE_NAME_TRANSACTION, updateValues, "account_no=?",new String[]{unSyncedTransactionList.get(i).getClientAccountNo()});
        }

        db.close();
    }

    /**
     * Delete all transaction after day end
     */
    public void deleteAllTransaction() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //delete all records in client table
        db.delete(DBHelper.TABLE_NAME_TRANSACTION, null, null);

        db.close();
    }

}
