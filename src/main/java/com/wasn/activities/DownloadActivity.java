package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;
import com.wasn.services.backgroundservices.ClientDataDownloadService;

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
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == download) {
            new ClientDataDownloadService(DownloadActivity.this).execute("1");
        } else if(view == skip) {
            // skip download and start mobile bank activity
            startActivity(new Intent(DownloadActivity.this, MobileBankActivity.class));
            DownloadActivity.this.finish();
        }

    }

}
