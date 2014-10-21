package com.wasn.services.backgroundservices;

import android.os.AsyncTask;
import com.wasn.activities.DownloadActivity;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.CannotProcessRequestException;
import com.wasn.exceptions.DataLostException;
import com.wasn.exceptions.ResponseErrorException;
import com.wasn.pojos.Client;
import com.wasn.services.DataCommunication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Background task that handles client data downloading
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class ClientDataDownloadService extends AsyncTask<String, String, String> {

    DownloadActivity activity;
    MobileBankApplication application;

    /**
     * Initialize cass members
     * @param activity
     */
    public ClientDataDownloadService(DownloadActivity activity) {
        this.activity = activity;
        application = (MobileBankApplication) activity.getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(String... strings) {
        String branchId = strings[0];

        // download data
        //return download(branchId);
        return downloadSampleData();
    }

    /**
     * Download client data from server
     * @param branchId branch id
     * @return download status
     */
    public String download(String branchId) {
        String downloadStatus = "0";

        // download and save client list
        try {
            ArrayList<Client> clientList = new DataCommunication().getClients(branchId);
            application.getMobileBankData().insetClients(clientList);

            // add client list to database
            application.getMobileBankData().insetClients(clientList);
            application.getMobileBankData().setDownloadState("1");

            downloadStatus = "1";
        } catch (URISyntaxException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (IOException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (CannotProcessRequestException e) {
            // cannot process request
            e.printStackTrace();
            downloadStatus = "-1";
        } catch (DataLostException e) {
            // data lost while downloading
            e.printStackTrace();
            downloadStatus = "-2";
        } catch (ResponseErrorException e) {
            // server response error
            e.printStackTrace();
            downloadStatus = "-3";
        } catch (android.database.SQLException e) {
            // database error
            e.printStackTrace();
            downloadStatus = "-4";
        }

        return downloadStatus;
    }

    /**
     * Insert sample data to database
     * this is for debug purpose
     */
    private String downloadSampleData() {
        ArrayList<Client> clientList = new ArrayList<Client>();

        // get sample client list
        for (int i=0; i<10; i++) {
            if(i == 1) {
                Client client = new Client("" + i, "Eranga Herath", "873643030V", "29/12/1987", "47899" + i, "3000", "450");
                clientList.add(client);
            } else if (i==1) {
                Client client = new Client("" + i, "Kanishka Silva", "889534345V", "20/10/1988", "47810" + i, "8500", "900");
                clientList.add(client);
            } else if (i==2) {
                Client client = new Client("" + i, "Dimuthu Perera", "909533349V", "07/11/1984", "57119" + i, "4050", "700");
                clientList.add(client);
            } else if (i==3) {
                Client client = new Client("" + i, "Buddika Ranaweera", "984534345V", "20/10/1988", "67610" + i, "10500", "1000");
                clientList.add(client);
            } else if (i==4) {
                Client client = new Client("" + i, "Buddika Ranaweera", "904534345V", "20/10/1988", "57610" + i, "10500", "1000");
                clientList.add(client);
            } else if (i==5) {
                Client client = new Client("" + i, "Kasun Janaka", "914533344V", "24/11/1982", "57611" + i, "1040", "900");
                clientList.add(client);
            } else if (i==6) {
                Client client = new Client("" + i, "Tharanga Bandara", "884534346V", "24/11/1988", "67611" + i, "11500", "700");
                clientList.add(client);
            } else if (i==7) {
                Client client = new Client("" + i, "Nalaka Witharana", "844534345V", "05/11/1989", "67612" + i, "1000", "800");
                clientList.add(client);
            } else if (i==8) {
                Client client = new Client("" + i, "Sanjaya Silva", "824534345V", "04/10/1982", "67613" + i, "8000", "1000");
                clientList.add(client);
            } else if (i==9) {
                Client client = new Client("" + i, "Prasanna Ekanayaka", "874534346V", "08/09/1987", "67614" + i, "7000", "900");
                clientList.add(client);
            }
        }

        // add client list to database
        application.getMobileBankData().insetClients(clientList);
        application.getMobileBankData().setDownloadState("1");

        return "1";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        activity.onPostDownload(status);
    }
}
