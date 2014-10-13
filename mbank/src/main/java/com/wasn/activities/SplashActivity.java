package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Splash activity, send login query from here
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 6000;
    private static final String TAG = SplashActivity.class.getName();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_layout);

        Typeface typefaceThin = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        TextView appName = (TextView) findViewById(R.id.splash_text);
        appName.setTypeface(typefaceThin, Typeface.BOLD);

        navigateToLogin();
    }

    /**
     * Navigate to login activity
     */
    private void navigateToLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}