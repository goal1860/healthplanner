package life.ppgoal.healthplanner.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import life.ppgoal.healthplanner.Item.SubCategoryList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashScreen extends AppCompatActivity {

    // splash screen timer
    private Method method;
    private static int SPLASH_TIME_OUT = 2000;
    private Boolean isCancelled = false;
    String diet_id = "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Method.forceRTLIfSupported(getWindow(), SplashScreen.this);

        method = new Method(SplashScreen.this);
        method.setStatusBarGradiant();

        if (getIntent().hasExtra("diet_id")) {
            diet_id = getIntent().getStringExtra("diet_id");
            Log.d("diet_id_notification", diet_id);
        }

        if (Method.isNetworkAvailable(SplashScreen.this)) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your ppgoal main activity
                    if (!isCancelled) {
                        if (diet_id.equals("0")) {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            aboutUs();
                        }
                    }

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            Toast.makeText(SplashScreen.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getResources().getString(R.string.internet_connection_message));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    public void notification() {

        Constant_Api.notificationSCL.clear();

        String url = Constant_Api.notification + diet_id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("DIET_APP");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String cid = object.getString("cat_id");
                        String diet_title = object.getString("diet_title");
                        String diet_info = object.getString("diet_info");
                        String diet_image = object.getString("diet_image");

                        Constant_Api.notificationSCL.add(new SubCategoryList(id, cid, diet_title, diet_info, diet_image));

                    }

                    startActivity(new Intent(SplashScreen.this, SubCategoryDetail.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("position", 0)
                            .putExtra("size", Constant_Api.notificationSCL.size())
                            .putExtra("day", Constant_Api.notificationSCL.get(0).getDiet_title())
                            .putExtra("type", "notification"));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void aboutUs() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.app_info, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String package_name = object.getString("package_name");
                        boolean isCorrectPackage = getApplication().getPackageName().equals(package_name);
                        isCorrectPackage = true;
                        if (isCorrectPackage) {
                            notification();
                        } else {
                            alertBoxSplashScreen(getResources().getString(R.string.contact_msg));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alertBoxSplashScreen(getResources().getString(R.string.contact_msg));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fail", "fail");
            }
        });
    }


    //---------------Alert Box---------------//

    public void alertBoxSplashScreen(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        isCancelled = true;
        super.onDestroy();
    }

}
