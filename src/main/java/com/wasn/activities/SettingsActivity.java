package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: eranga
 * Date: 11/14/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {

    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout save;
    TextView headerText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        init();
    }

    public void init() {
        back = (RelativeLayout) findViewById(R.id.settings_layout_back);
        help = (RelativeLayout) findViewById(R.id.settings_layout_help);
        save = (RelativeLayout) findViewById(R.id.settings_layout_save);
        headerText = (TextView) findViewById(R.id.settings_layout_header_text);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(SettingsActivity.this);
        help.setOnClickListener(SettingsActivity.this);
        save.setOnClickListener(SettingsActivity.this);
    }

    public void onClick(View view) {
        if(view == back) {
            // back to main activity
            startActivity(new Intent(SettingsActivity.this, MobileBankActivity.class));
            SettingsActivity.this.finish();
        } else if(view == help) {

        }
    }
}
