package life.ppgoal.healthplanner.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutUs extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Method.forceRTLIfSupported(getWindow(), AboutUs.this);

        Method method = new Method(AboutUs.this);
        method.setStatusBarGradiant();

        toolbar = findViewById(R.id.toolbar_about_us);
        toolbar.setTitle(getResources().getString(R.string.about_us));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView_app_name = findViewById(R.id.textView_app_name_about_us);
        TextView textView_app_version = findViewById(R.id.textView_app_version_about_us);
        TextView textView_app_author = findViewById(R.id.textView_app_author_about_us);
        TextView textView_app_contact = findViewById(R.id.textView_app_contact_about_us);
        TextView textView_app_email = findViewById(R.id.textView_app_email_about_us);
        TextView textView_app_website = findViewById(R.id.textView_app_website_about_us);
        WebView webView = findViewById(R.id.webView_about_us);
        LinearLayout linearLayout = findViewById(R.id.linearLayout_about_us);

        ImageView app_logo = findViewById(R.id.app_logo_about_us);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, AboutUs.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, AboutUs.this);
        }

        if (Constant_Api.aboutUsList != null) {

            Picasso.get().load(Constant_Api.image + Constant_Api.aboutUsList.getApp_logo())
                    .into(app_logo);

            textView_app_name.setText(Constant_Api.aboutUsList.getApp_name());

            textView_app_version.setText(Constant_Api.aboutUsList.getApp_version());
            textView_app_author.setText(Constant_Api.aboutUsList.getApp_author());
            textView_app_contact.setText(Constant_Api.aboutUsList.getApp_contact());
            textView_app_email.setText(Constant_Api.aboutUsList.getApp_email());
            textView_app_website.setText(Constant_Api.aboutUsList.getApp_website());

            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setFocusableInTouchMode(false);
            webView.setFocusable(false);
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            String mimeType = "text/html";
            String encoding = "utf-8";
            String htmlText = Constant_Api.aboutUsList.getApp_description();

            String text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/latoregular.ttf\")}body{font-family: MyFont;color: #8b8b8b;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
