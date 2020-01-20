package com.app.dietplan.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dietplan.R;
import com.app.dietplan.Util.Constant_Api;
import com.app.dietplan.Util.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PrivacyPolice extends AppCompatActivity {

    public Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Method.forceRTLIfSupported(getWindow(), PrivacyPolice.this);

        Method method = new Method(PrivacyPolice.this);
        method.setStatusBarGradiant();

        toolbar = findViewById(R.id.toolbar_privacy_policy);
        toolbar.setTitle(getResources().getString(R.string.privacy_policy));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = findViewById(R.id.textview_privacy_policy);
        LinearLayout linearLayout = findViewById(R.id.linearLayout_privacy_policy);

        if (Constant_Api.aboutUsList != null) {
            textView.setText(Html.fromHtml(Constant_Api.aboutUsList.getApp_privacy_policy()));
        }

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, PrivacyPolice.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, PrivacyPolice.this);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
