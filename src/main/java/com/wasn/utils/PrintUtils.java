package com.wasn.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.wasn.exceptions.BluetoothNotAvailableException;
import com.wasn.exceptions.BluetoothNotEnableException;
import com.wasn.exceptions.CannotConnectToPrinterException;
import com.wasn.exceptions.CannotPrintException;
import com.wasn.pojos.Summary;
import com.wasn.pojos.Transaction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Class deal with generic printing functionality
 *
 * @author eranga.herath@pagero.com (eranga herath)
 */
public class PrintUtils {

    //printing service UUID
    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // printer bluetooth address
    private static String bluetoothAddress = "00:22:58:38:AE:90";

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

    /**
     * Print receipt via bluetooth
     * @param transaction printing transaction
     * @throws IOException
     * @throws BluetoothNotEnableException
     * @throws BluetoothNotAvailableException
     * @throws CannotConnectToPrinterException
     * @throws CannotPrintException
     */
    public static void printReceipt(Transaction transaction) throws IOException, BluetoothNotEnableException, BluetoothNotAvailableException, CannotConnectToPrinterException, CannotPrintException {
        OutputStream outputStream = connectToPrinter();

        // receipt header
        String address      = ".           SANASA Development Bank\r\n";
        String branch       = "                  colombo - 02     \r\n";
        String telephoneNo  = "                Tel: 011 2393759   \r\n";

        // printing receipt type
        // customer copy/ agent copy
        String receiptType  = "                 (Customer copy)    \r\n\r\n";

        // logo printing command
        // ZPL(Zebra Program Language)
        String printLogoCommand = "! 0 200 200 150 1\r\n"
                                + "EG 8 128 250 0 "
                                + "000000000000000000000000000000001FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFF03FFFFFFF1FFFFF003FFFFFFF1FFFF80FFFFFFFFF1FFF81F0007FFFFF1FFE0F01F803FFFF1FF8781FFFE07FFF1FF1E1E0001E0FFF1FE38703FE03C3FF1FC61C3F8FF070FF1F8E18F8007C38FF1F0C71E000070C3F1F18C7801FC1871F1E318F001FF8638F1E310E001FFE31871E731E0007FF18C71E631E0001FF88E31E621F80001F8C631E631FC0000FC4631E731FF0000FC4631E318FFC00078C631F1887FF00078C631F18E3FF80070CE31F8C70FF800F18C71FC70C1F001E71871FE387000078E18F1FF1C3C001F0C30F1FFC707F3F83861F1FFE1E07FC0E1C7F1FFF83C0007078FF1FFFF01FFF03C3FF1FFFFF00001F07FF1FFFFFF801F81FFF1FFFFFFFFF01FFFF1FFFFFFFE003FFFF1FFFFFFFE1FFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF000000000000000000000000000000000000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFF9FFF0FE003FF1FF000FF0FE000FF1FE0007F0FE0007F1FE0007F0FE0007F1FC0F03F0FE0E03F1FC0FFFF0FE0F81F1FC007FF0FE0F81F1FE001FF0FE0F81F1FE0007F0FE0F81F1FF0003F0FE0F81F1FFC001F0FE0F81F1FFFF01F0FE0F81F1FC0F81F0FE0F81F1F80F01F0FE0F03F1FC0003F0FE0007F1FE0007F0FE0007F1FF000FF0FE000FF1FFC07FF0FC003FF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF0000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FC00FFF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F8000FF0FE0FFFF1F81E0FF0FE0FFFF1F81E0FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F81F07F0FE0FFFF1F81F07F0FE0FFFF1F80C07F0FE0007F1F8000FF0FE0007F1F8000FF0FE0007F1FC007FF0FE0007F1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF\r\n"
                                + "PRINT\r\n";

        // transaction details that print in receipt
        String name   = "      Name               : "+transaction.getClinetName()+"\r\n";
        String nic    = "      NIC no             : "+transaction.getClinetNic()+"\r\n";
        String accNo  = "      Account No         : "+transaction.getClientAccountNo()+"\r\n";
        String type   = "      Transaction type   : "+transaction.getTransactionType()+"\r\n";
        String time   = "      Transaction time   : "+transaction.getTransactionTime()+"\r\n";
        String amount = "      Transaction amount : "+transaction.getTransactionAmount()+"\r\n";
        String balance= "      Balance            : "+transaction.getCurrentBalance()+"\r\n";
        String recNo  = "      Receipt No         : "+transaction.getReceiptId()+"\r\n\r\n\r\n";

        String sign   = "..................          ..................\r\n";
        String end    = "    Customer                       Agent      \r\n\r\n\r\n";

        // send dat to printer
        // send data as byte stream
        try {
            // print customer copy
            outputStream.write(printLogoCommand.getBytes());
            outputStream.write(address.getBytes());
            outputStream.write(branch.getBytes());
            outputStream.write(telephoneNo.getBytes());
            outputStream.write(receiptType.getBytes());
            outputStream.write(name.getBytes());
            outputStream.write(nic.getBytes());
            outputStream.write(accNo.getBytes());
            outputStream.write(type.getBytes());
            outputStream.write(time.getBytes());
            outputStream.write(amount.getBytes());
            outputStream.write(balance.getBytes());
            outputStream.write(recNo.getBytes());
            outputStream.write(sign.getBytes());
            outputStream.write(end.getBytes());

            //empty for loop for accurate use
            for(int i=0;i<10000;i++) {

            }
            Thread.sleep(10000);

            receiptType = "                  (Agent copy)      \r\n\r\n";

            // print agent copy
            outputStream.write(printLogoCommand.getBytes());
            outputStream.write(address.getBytes());
            outputStream.write(branch.getBytes());
            outputStream.write(telephoneNo.getBytes());
            outputStream.write(receiptType.getBytes());
            outputStream.write(name.getBytes());
            outputStream.write(nic.getBytes());
            outputStream.write(accNo.getBytes());
            outputStream.write(type.getBytes());
            outputStream.write(time.getBytes());
            outputStream.write(amount.getBytes());
            outputStream.write(balance.getBytes());
            outputStream.write(recNo.getBytes());
            outputStream.write(sign.getBytes());
            outputStream.write(end.getBytes());

            //empty for loop for accurate use
            for(int i=0;i<10000;i++){

            }
            Thread.sleep(2000);

            outputStream.flush();
        } catch(Exception e) {
            throw new CannotPrintException();
        } finally {
            // close streams
            try {
                outputStream.close();
            } catch(Exception e) {
                throw new CannotPrintException();
            }
        }
    }

    /**
     * Re-print receipt
     * @param transaction transaction to re-print
     * @throws IOException
     * @throws BluetoothNotEnableException
     * @throws BluetoothNotAvailableException
     * @throws CannotConnectToPrinterException
     * @throws CannotPrintException
     */
    public static void rePrintReceipt(Transaction transaction) throws IOException, BluetoothNotEnableException, BluetoothNotAvailableException, CannotConnectToPrinterException, CannotPrintException {
        OutputStream outputStream = connectToPrinter();

        // receipt header
        String address      = ".           SANASA Development Bank\r\n";
        String branch       = "                  colombo - 02     \r\n";
        String telephoneNo  = "                Tel: 011 2393759   \r\n";

        // printing receipt type
        // customer copy/ agent copy
        String receiptType  = "           (Reprint Agent/Customer copy)      \r\n\r\n";

        // logo printing command
        // ZPL(Zebra Program Language)
        String printLogoCommand = "! 0 200 200 150 1\r\n"
                + "EG 8 128 250 0 "
                + "000000000000000000000000000000001FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFF03FFFFFFF1FFFFF003FFFFFFF1FFFF80FFFFFFFFF1FFF81F0007FFFFF1FFE0F01F803FFFF1FF8781FFFE07FFF1FF1E1E0001E0FFF1FE38703FE03C3FF1FC61C3F8FF070FF1F8E18F8007C38FF1F0C71E000070C3F1F18C7801FC1871F1E318F001FF8638F1E310E001FFE31871E731E0007FF18C71E631E0001FF88E31E621F80001F8C631E631FC0000FC4631E731FF0000FC4631E318FFC00078C631F1887FF00078C631F18E3FF80070CE31F8C70FF800F18C71FC70C1F001E71871FE387000078E18F1FF1C3C001F0C30F1FFC707F3F83861F1FFE1E07FC0E1C7F1FFF83C0007078FF1FFFF01FFF03C3FF1FFFFF00001F07FF1FFFFFF801F81FFF1FFFFFFFFF01FFFF1FFFFFFFE003FFFF1FFFFFFFE1FFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF000000000000000000000000000000000000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFF9FFF0FE003FF1FF000FF0FE000FF1FE0007F0FE0007F1FE0007F0FE0007F1FC0F03F0FE0E03F1FC0FFFF0FE0F81F1FC007FF0FE0F81F1FE001FF0FE0F81F1FE0007F0FE0F81F1FF0003F0FE0F81F1FFC001F0FE0F81F1FFFF01F0FE0F81F1FC0F81F0FE0F81F1F80F01F0FE0F03F1FC0003F0FE0007F1FE0007F0FE0007F1FF000FF0FE000FF1FFC07FF0FC003FF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF0000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FC00FFF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F8000FF0FE0FFFF1F81E0FF0FE0FFFF1F81E0FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F81F07F0FE0FFFF1F81F07F0FE0FFFF1F80C07F0FE0007F1F8000FF0FE0007F1F8000FF0FE0007F1FC007FF0FE0007F1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF\r\n"
                + "PRINT\r\n";

        // transaction details that print in receipt
        String name   = "      Name               : "+transaction.getClinetName()+"\r\n";
        String nic    = "      NIC no             : "+transaction.getClinetNic()+"\r\n";
        String accNo  = "      Account No         : "+transaction.getClientAccountNo()+"\r\n";
        String type   = "      Transaction type   : "+transaction.getTransactionType()+"\r\n";
        String time   = "      Transaction time   : "+transaction.getTransactionTime()+"\r\n";
        String amount = "      Transaction amount : "+transaction.getTransactionAmount()+"\r\n";
        String balance= "      Balance            : "+transaction.getCurrentBalance()+"\r\n";
        String recNo  = "      Receipt No         : "+transaction.getReceiptId()+"\r\n\r\n\r\n";

        String sign   = "..................          ..................\r\n";
        String end    = "    Customer                       Agent      \r\n\r\n\r\n";

        // send dat to printer
        // send data as byte stream
        try {
            // print customer copy
            outputStream.write(printLogoCommand.getBytes());
            outputStream.write(address.getBytes());
            outputStream.write(branch.getBytes());
            outputStream.write(telephoneNo.getBytes());
            outputStream.write(receiptType.getBytes());
            outputStream.write(name.getBytes());
            outputStream.write(nic.getBytes());
            outputStream.write(accNo.getBytes());
            outputStream.write(type.getBytes());
            outputStream.write(time.getBytes());
            outputStream.write(amount.getBytes());
            outputStream.write(balance.getBytes());
            outputStream.write(recNo.getBytes());
            outputStream.write(sign.getBytes());
            outputStream.write(end.getBytes());

            //empty for loop for accurate use
            for(int i=0;i<10000;i++){

            }
            Thread.sleep(2000);

            outputStream.flush();
        } catch(Exception e) {
            throw new CannotPrintException();
        } finally {
            // close streams
            try {
                outputStream.close();
            } catch(Exception e) {
                throw new CannotPrintException();
            }
        }
    }

    /**
     * Print summary
     * @param summary summary object
     * @throws BluetoothNotEnableException
     * @throws IOException
     * @throws CannotConnectToPrinterException
     * @throws BluetoothNotAvailableException
     * @throws CannotPrintException
     */
    public static void printSummary(Summary summary) throws BluetoothNotEnableException, IOException, CannotConnectToPrinterException, BluetoothNotAvailableException, CannotPrintException {
        OutputStream outputStream = connectToPrinter();

        // receipt header
        String address      = ".           SANASA Development Bank\r\n";
        String branch       = "                  colombo - 02     \r\n";
        String telephoneNo  = "                Tel: 011 2393759   \r\n";

        // printing receipt type
        // customer copy/ agent copy
        String receiptType  = "           (Reprint Agent/Customer copy)      \r\n\r\n";

        // logo printing command
        // ZPL(Zebra Program Language)
        String printLogoCommand = "! 0 200 200 150 1\r\n"
                + "EG 8 128 250 0 "
                + "000000000000000000000000000000001FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFF03FFFFFFF1FFFFF003FFFFFFF1FFFF80FFFFFFFFF1FFF81F0007FFFFF1FFE0F01F803FFFF1FF8781FFFE07FFF1FF1E1E0001E0FFF1FE38703FE03C3FF1FC61C3F8FF070FF1F8E18F8007C38FF1F0C71E000070C3F1F18C7801FC1871F1E318F001FF8638F1E310E001FFE31871E731E0007FF18C71E631E0001FF88E31E621F80001F8C631E631FC0000FC4631E731FF0000FC4631E318FFC00078C631F1887FF00078C631F18E3FF80070CE31F8C70FF800F18C71FC70C1F001E71871FE387000078E18F1FF1C3C001F0C30F1FFC707F3F83861F1FFE1E07FC0E1C7F1FFF83C0007078FF1FFFF01FFF03C3FF1FFFFF00001F07FF1FFFFFF801F81FFF1FFFFFFFFF01FFFF1FFFFFFFE003FFFF1FFFFFFFE1FFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF1FFFFFFFFFFFFFFF000000000000000000000000000000000000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFF9FFF0FE003FF1FF000FF0FE000FF1FE0007F0FE0007F1FE0007F0FE0007F1FC0F03F0FE0E03F1FC0FFFF0FE0F81F1FC007FF0FE0F81F1FE001FF0FE0F81F1FE0007F0FE0F81F1FF0003F0FE0F81F1FFC001F0FE0F81F1FFFF01F0FE0F81F1FC0F81F0FE0F81F1F80F01F0FE0F03F1FC0003F0FE0007F1FE0007F0FE0007F1FF000FF0FE000FF1FFC07FF0FC003FF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF0000000000000000000000000000000000000000000000001FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FC00FFF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F8000FF0FE0FFFF1F81E0FF0FE0FFFF1F81E0FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8001FF0FE0FFFF1F8000FF0FE0FFFF1F81F07F0FE0FFFF1F81F07F0FE0FFFF1F80C07F0FE0007F1F8000FF0FE0007F1F8000FF0FE0007F1FC007FF0FE0007F1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF1FFFFFFF0FFFFFFF\r\n"
                + "PRINT\r\n";

        // transaction details that print in receipt
        //String branch = "      Branch Id          : "+summary.getBranchId()+"\r\n";
        String depositCount  = "      Deposit count      : "+summary.getTransactionCount()+"\r\n";
        String depositAmount = "      Deposit amount     : "+summary.getTotalTransactionAmount()+"\r\n\r\n\r\n";

        String sign          = "             ....................             \r\n";
        String end           = "                    Agent             \r\n\r\n\r\n";

        // send dat to printer
        // send data as byte stream
        try {
            // print customer copy
            outputStream.write(printLogoCommand.getBytes());
            outputStream.write(address.getBytes());
            outputStream.write(branch.getBytes());
            outputStream.write(telephoneNo.getBytes());
            outputStream.write(receiptType.getBytes());
            outputStream.write(depositCount.getBytes());
            outputStream.write(depositAmount.getBytes());
            outputStream.write(sign.getBytes());
            outputStream.write(end.getBytes());

            //empty for loop for accurate use
            for(int i=0;i<10000;i++){

            }
            Thread.sleep(2000);

            outputStream.flush();
        } catch(Exception e) {
            throw new CannotPrintException();
        } finally {
            // close streams
            try {
                outputStream.close();
            } catch(Exception e) {
                throw new CannotPrintException();
            }
        }
    }
}
