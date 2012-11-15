package com.wasn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class deal with generic network and IO functionality
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class NetworkUtil {

    /**
     * Check network connection availability
     *
     * @param context need to access ConnectivityManager
     * @return availability of network
     */
    public static boolean isAvailableNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert input stream to string
     *
     * @param inputStream stream need to be convert
     * @return converted string of stream
     */
    public static String convertStreamToString(InputStream inputStream) throws IOException {
        // to read data from stream
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // to store reading data
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        // read data, can raise IO Exception
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        return stringBuilder.toString();
    }

}