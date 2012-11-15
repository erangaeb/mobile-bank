package com.wasn.services;

import com.wasn.exceptions.CannotProcessRequestException;
import com.wasn.exceptions.DataLostException;
import com.wasn.exceptions.ResponseErrorException;
import com.wasn.pojos.Client;
import com.wasn.utils.NetworkUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Class that deals with generic data communication functionalities with backend
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class DataCommunication {

    private static final String DOWNLOAD_URL = "http://10.100.31.5:8080/MBank_Server/SinkServlet";
    private static final String SYNC_URL = "";

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
        ArrayList<Client> clientList = new ArrayList<Client>();

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
                clientList = parserServerResponse(serverResponse);
            }
        } else {
            throw new CannotProcessRequestException();
        }

        return clientList;
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
}
