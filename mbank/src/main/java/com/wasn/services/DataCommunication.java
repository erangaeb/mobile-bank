package com.wasn.services;

import com.wasn.exceptions.*;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;
import com.wasn.utils.NetworkUtil;
import com.wasn.utils.SHA1;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Class that deals with generic data communication functionality with backend
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class DataCommunication {

    private static final String LOGIN_URL = "http://10.100.31.5:8080/MBank_Server/LoginServlet";
    private static final String DOWNLOAD_URL = "http://10.100.31.5:8080/MBank_Server/SinkServlet";
    private static final String SYNC_URL = "http://10.100.31.5:8080/MBank_Server/TransactionServlet";

    /**
     * Authentication user
     * @param username username
     * @param password password
     * @return branch id
     * @throws IOException
     * @throws URISyntaxException
     * @throws CannotProcessRequestException
     * @throws UnAuthenticatedUserException
     */
    public int authenticateUser(String username, String password) throws IOException, URISyntaxException, CannotProcessRequestException, UnAuthenticatedUserException {
        // get request to server
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet;
        URI uri = new URI(LOGIN_URL + "?username=" +username+"&" + "password="+ SHA1.encode(password));
        httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpclient.execute(httpGet);
        int status = httpResponse.getStatusLine().getStatusCode();

        // successful request
        if(status == HttpStatus.SC_OK) {
            // extract response from server
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                String serverResponse = NetworkUtil.convertStreamToString(inputStream);

                // get branch id from response
                int branchId = Integer.parseInt(serverResponse.replace("\n",""));

                if(branchId == 0) {
                    throw new UnAuthenticatedUserException();
                } if(branchId < 0) {
                    throw new CannotProcessRequestException();
                } else {
                    // user authenticated
                    // valid user
                    return branchId;
                }
            } else {
                throw new CannotProcessRequestException();
            }
        } else {
            throw new CannotProcessRequestException();
        }
    }


    /**
     * Get client details from server
     * @param branchId user's branch id
     * @return clientList downloaded clients
     * @throws URISyntaxException
     * @throws IOException
     * @throws CannotProcessRequestException
     * @throws DataLostException
     * @throws ResponseErrorException
     */
    public ArrayList<Client> getClients(String branchId) throws URISyntaxException, IOException, CannotProcessRequestException, DataLostException, ResponseErrorException {
        // get request to server
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet;
        URI uri = new URI(DOWNLOAD_URL + "?branchId=" + branchId);
        httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpclient.execute(httpGet);
        int status = httpResponse.getStatusLine().getStatusCode();

        // successful request
        if(status == HttpStatus.SC_OK) {
            // extract response from server
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                String serverResponse = NetworkUtil.convertStreamToString(inputStream);

                // get client list by parsing server response
                return parserServerResponse(serverResponse);
            } else {
                throw new CannotProcessRequestException();
            }
        } else {
            throw new CannotProcessRequestException();
        }
    }

    /**
     * sync transactions
     * @param transactionList un synced transaction list
     * @return synced records count
     * @throws URISyntaxException
     * @throws IOException
     * @throws CannotProcessRequestException
     * @throws ErrorInSyncedRecordException
     * @throws ErrorInServerException
     */
    public int syncTransactions(ArrayList<Transaction> transactionList) throws URISyntaxException, IOException, CannotProcessRequestException, ErrorInSyncedRecordException, ErrorInServerException {
        // get request to server
        HttpClient httpclient = new DefaultHttpClient();
        URI uri = new URI(DOWNLOAD_URL);
        HttpPost httpPost = new HttpPost(uri);

        StringEntity entity = new StringEntity(getSyncRecord(transactionList));
        httpPost.setEntity(entity);

        HttpResponse httpResponse =httpclient.execute(httpPost);
        int status = httpResponse.getStatusLine().getStatusCode();

        // successful request
        if(status == HttpStatus.SC_OK) {
            // extract response from server
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                String serverResponse = NetworkUtil.convertStreamToString(inputStream);

                // error in synced record
                if(serverResponse.equals("-2") || serverResponse.equals("-5")) {
                    throw new ErrorInSyncedRecordException();
                } else if(serverResponse.equals("-1")) {
                    throw new ErrorInServerException();
                }

                return Integer.parseInt(serverResponse);
            } else {
                throw new CannotProcessRequestException();
            }
        } else {
            throw new CannotProcessRequestException();
        }
    }

    /**
     * Parser server response and extract client list
     * @param serverResponse server response
     * @return client list
     * @throws ResponseErrorException
     * @throws DataLostException
     */
    private ArrayList<Client> parserServerResponse(String serverResponse) throws ResponseErrorException, DataLostException {
        // keeps track with no of records sends by backend
        int handshake = 0;

        ArrayList<Client> clientList = new ArrayList<Client>();

        // client entries comes with '\n' separator
        // split by '\n'
        String entryDelimiter = "\n";
        String[] entries = serverResponse.split(entryDelimiter);

        try {
            for(int i=0; i<entries.length; i++) {
                if(i==0) {
                    // handshake comes in initial entry
                    handshake = Integer.parseInt(entries[i]);
                } else {
                    // split record and extract client records
                    // split by ','
                    String attributedDelimiter = ",";
                    String[] attributes = entries[i].split(attributedDelimiter);

                    // extract client details
                    String clientId = attributes[0];
                    String clientName = attributes[1];
                    String nic = attributes[2];
                    String accountNo = attributes[3];
                    String balanceAmount = attributes[4];
                    String birthDate = attributes[5];
                    String previousTransaction = attributes[6];

                    Client client = new Client(clientId, clientName, nic, birthDate,accountNo, balanceAmount, previousTransaction);
                    clientList.add(client);
                }
            }
        }catch (Exception e) {
            // error in server response
            throw new ResponseErrorException();
        }

        // lost some data
        if(handshake != clientList.size()) {
            throw new DataLostException();
        }

        return clientList;
    }

    /**
     * Create '\n' separated string with transaction details
     * @param transactionList un-synced transaction list
     * @return record
     */
    private String getSyncRecord(ArrayList<Transaction> transactionList) {
        String syncRecord = "";

        // initially add transaction count
        syncRecord = syncRecord + "sync"+" "+transactionList.size()+"\n";

        // append transaction records
        for(int i=0;i<transactionList.size();i++){
            Transaction transaction=transactionList.get(i);

            //sending record consists \n
            String transactionRecord = transaction.getTransactionTime().replace(" ", "  ")+","+
                                       transaction.getClientAccountNo()+","+
                                       transaction.getClientId()+","+
                                       transaction.getTransactionType()+","+
                                       transaction.getTransactionAmount()+","+
                                       "empty"+","+
                                       transaction.getBranchId()+","+
                                       transaction.getReceiptId()+","+
                                       "empty"+"\n";
            syncRecord = syncRecord + transactionRecord;
        }

        return syncRecord;
    }
}
