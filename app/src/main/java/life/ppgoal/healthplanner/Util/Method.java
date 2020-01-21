package life.ppgoal.healthplanner.Util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Method {

    public Activity activity;
    public static boolean share = false, onBackPress = false, permissionStorage = false, personalization_ad = false;
    private static Bitmap mbitmap;
    private static File file;
    private AdShow adShow;

    public static Typeface scriptable;

    public Method(Activity activity) {
        this.activity = activity;
        scriptable = Typeface.createFromAsset(activity.getAssets(), "font/latobold.ttf");
    }

    public Method(Activity activity, AdShow adShow) {
        this.activity = activity;
        scriptable = Typeface.createFromAsset(activity.getAssets(), "font/latobold.ttf");
        this.adShow = adShow;
    }


    //rtl
    public static void forceRTLIfSupported(Window window, Activity activity) {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //screen get height and width
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    //status bar
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarGradiant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /*<---------------------- share & download and method ---------------------->*/

    public void share_save(String link, String title, String description) {
        new DownloadImageTask().execute(link, title, description);
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadImageTask extends AsyncTask<String, String, String> {

        String title, description;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                title = params[1];
                description = params[2];
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mbitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (share) {
                ShareImage(mbitmap, title, description);
                share = false;
            }
            super.onPostExecute(s);
        }
    }

    @SuppressLint("IntentReset")
    private void ShareImage(Bitmap finalBitmap, String title, String description) {

        String root = activity.getExternalCacheDir().getAbsolutePath();
        String fname = "Image_share" + ".jpg";
        file = new File(root, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri contentUri = Uri.fromFile(file);
        Log.d("url", String.valueOf(contentUri));

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving ppgoal to read this file
            shareIntent.setData(contentUri);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(title) + "\n" + "\n" + Html.fromHtml(description));
            activity.startActivity(Intent.createChooser(shareIntent, "Choose an ppgoal"));
        }

    }

    /*<---------------------- share & download and method ---------------------->*/

    public static void isOnBackPress() {
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    //---------------Interstitial Ad---------------//

    public void interstitialAdShow(final int position, final String type, final String size, final String day, final String id, final String image) {

        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isInterstital_ad()) {
                Constant_Api.AD_COUNT = Constant_Api.AD_COUNT + 1;
                if (Constant_Api.AD_COUNT == Constant_Api.AD_COUNT_SHOW) {
                    Constant_Api.AD_COUNT = 0;
                    final InterstitialAd interstitialAd = new InterstitialAd(activity);
                    AdRequest adRequest;
                    if (Method.personalization_ad) {
                        adRequest = new AdRequest.Builder()
                                .build();
                    } else {
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        adRequest = new AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                .build();
                    }
                    if (Constant_Api.aboutUsList != null) {
                        interstitialAd.setAdUnitId(Constant_Api.aboutUsList.getInterstital_ad_id());
                    } else {
                        interstitialAd.setAdUnitId("");
                    }
                    interstitialAd.loadAd(adRequest);
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            interstitialAd.show();
                        }

                        public void onAdClosed() {
                            adShow.position(position, type, size, day, id, image);
                            super.onAdClosed();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            Log.d("admob_error", String.valueOf(i));
                            adShow.position(position, type, size, day, id, image);
                            super.onAdFailedToLoad(i);
                        }

                    });
                } else {
                    adShow.position(position, type, size, day, id, image);
                }
            } else {
                adShow.position(position, type, size, day, id, image);
            }
        } else {
            adShow.position(position, type, size, day, id, image);
        }
    }

    //---------------Interstitial Ad---------------//

    //---------------Banner Ad---------------//

    public static void showPersonalizedAds(LinearLayout linearLayout, Activity activity) {

        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isBanner_ad()) {
                AdView adView = new AdView(activity);
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                adView.setAdUnitId(Constant_Api.aboutUsList.getBanner_ad_id());
                adView.setAdSize(AdSize.BANNER);
                linearLayout.addView(adView);
                adView.loadAd(adRequest);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public static void showNonPersonalizedAds(LinearLayout linearLayout, Activity activity) {

        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isBanner_ad()) {
                AdView adView = new AdView(activity);
                AdRequest adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                        .build();
                adView.setAdUnitId(Constant_Api.aboutUsList.getBanner_ad_id());
                adView.setAdSize(AdSize.BANNER);
                linearLayout.addView(adView);
                adView.loadAd(adRequest);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    //---------------Banner Ad---------------//

    //---------------Alert Box---------------//

    public void alertBox(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(activity.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    //---------------Alert Box---------------//


    //---------------Share bmi result---------------//

    public void share_bmi(String score, String result) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving ppgoal to read this file
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.your_bmi_score) + " " + "=" + " " + score
                + "\n"
                + activity.getResources().getString(R.string.status) + " " + "=" + " " + result
                + "\n" + "\n" + "https://play.google.com/store/apps/details?id=" + activity.getApplication().getPackageName());
        activity.startActivity(Intent.createChooser(shareIntent, "Choose an ppgoal"));

    }

    //---------------Share bmi result---------------//

}
