package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.wasn.application.MobileBankApplication;
import com.wasn.services.backgroundservices.ClientDataDownloadService;
import com.wasn.utils.NetworkUtil;

/**
 * Activity class correspond to download
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class DownloadActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // activity components
    TextView headerText;
    TextView informationText;
    TextView questionText;
    RelativeLayout download;
    RelativeLayout skip;

    // display when downloading
    public ProgressDialog progressDialog;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) DownloadActivity.this.getApplication();

        // initialize
        headerText = (TextView) findViewById(R.id.download_layout_header_text);
        informationText = (TextView) findViewById(R.id.download_layout_information_text);
        questionText = (TextView) findViewById(R.id.download_layout_question_text);
        download = (RelativeLayout) findViewById(R.id.download_layout_download);
        skip = (RelativeLayout) findViewById(R.id.download_layout_skip);

        // set custom font to texts
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        informationText.setTypeface(face);
        questionText.setTypeface(face);
        questionText.setTypeface(null, Typeface.BOLD);

        // set click listeners
        download.setOnClickListener(DownloadActivity.this);
        skip.setOnClickListener(DownloadActivity.this);

        if(application.getMobileBankData().getDownloadState().endsWith("1")) {
            // already have downloaded data
            // enable question text
            // enable skip
            questionText.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
        } else {
            // no downloaded data
            questionText.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
        }
    }

    /**
     * Download data from server
     */
    public void download() {
        // download data if only available network connection
        if(NetworkUtil.isAvailableNetwork(DownloadActivity.this)) {
            // start background thread to download data
            progressDialog = ProgressDialog.show(DownloadActivity.this, "", "Downloading client data from bank server");
            new ClientDataDownloadService(DownloadActivity.this).execute(application.getMobileBankData().getBranchId());
        } else {
            displayToast("No network connection");
        }
    }

    /**
     * Close progress dialog
     */
    public void closeProgressDialog() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    /**
     * execute after downloading client details
     * @param status download status
     */
    public void onPostDownload(String status) {
        closeProgressDialog();

        // display toast according to download status
        if(status.equals("1")) {
            displayToast("Successfully downloaded data");
        } else if(status.equals("-2")) {
            displayToast("Data lost while downloading");
        } else if(status.equals("-3")) {
            displayToast("Server response error");
        } else if(status.equals("-4")) {
            displayToast("Error in mobile database");
        } else {
            displayToast("Cannot process request");
        }

        // start mobile bank activity
        startActivity(new Intent(DownloadActivity.this, MobileBankActivity.class));
        DownloadActivity.this.finish();
    }

    /**
     * Display toast message
     * @param message message tobe display
     */
    public void displayToast(String message) {
        Toast.makeText(DownloadActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == download) {
            download();
        } else if(view == skip) {
            // skip download and start mobile bank activity
            startActivity(new Intent(DownloadActivity.this, MobileBankActivity.class));
            DownloadActivity.this.finish();
        }
    }

}
