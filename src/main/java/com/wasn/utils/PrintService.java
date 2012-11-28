package com.wasn.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.wasn.exceptions.BluetoothNotAvailableException;
import com.wasn.exceptions.BluetoothNotEnableException;
import com.wasn.exceptions.CannotConnectToPrinterException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Class deal with generic printing functionality
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class PrintService {

    //printing service UUID
    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // printer bluetooth address
    private static String bluetoothAddress = "00:03:7A:1A:1B:E7";

    /**
     * Get bluetooth adaoer to deal with bluetooth related functionalities
     * @return bluetooth adapter
     */
    public static BluetoothAdapter getBluetoothAdapter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return bluetoothAdapter;
    }

    /**
     * check weather bluetooth enabled
     * @return state
     * @throws BluetoothNotEnableException
     * @throws BluetoothNotAvailableException
     */
    public static boolean isEnableBluetooth() throws BluetoothNotEnableException, BluetoothNotAvailableException {
        if(getBluetoothAdapter() == null) {
            throw new BluetoothNotAvailableException();
        } else {
            if(!getBluetoothAdapter().isEnabled()) {
                throw new BluetoothNotEnableException();
            }
        }

        return true;
    }

    /**
     * Connect to printer
     * @return outputStream
     * @throws BluetoothNotEnableException
     * @throws BluetoothNotAvailableException
     * @throws IOException
     * @throws CannotConnectToPrinterException
     */
    public static OutputStream connectToPrinter() throws BluetoothNotEnableException, BluetoothNotAvailableException, IOException, CannotConnectToPrinterException {
        OutputStream outputStream = null;

        if(isEnableBluetooth()) {
            // bluetooth configurations
            BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress);
            BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SERVICE_UUID);

            bluetoothAdapter.cancelDiscovery();
            bluetoothSocket.connect();

            outputStream = bluetoothSocket.getOutputStream();
        }

        if(outputStream == null) {
            throw new CannotConnectToPrinterException();
        } else {
            return outputStream;
        }
    }
}
