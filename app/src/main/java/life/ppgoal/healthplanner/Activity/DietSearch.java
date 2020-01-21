package life.ppgoal.healthplanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import life.ppgoal.healthplanner.Adapter.SubCategoryAdapter;
import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.SubCategoryList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DietSearch extends AppCompatActivity {

    private Method method;
    public Toolbar toolbar;
    private AdShow adShow;
    private String search;
    private RecyclerView recyclerView;
    private TextView textView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_search);

        Method.forceRTLIfSupported(getWindow(), DietSearch.this);

        Constant_Api.searchList = new ArrayList<>();

        adShow = new AdShow() {
            @Override
            public void position(int position, String type, String size, String day, String id, String image) {
                startActivity(new Intent(DietSearch.this, SubCategoryDetail.class)
                        .putExtra("position", position)
                        .putExtra("size", Integer.parseInt(size))
                        .putExtra("day", day)
                        .putExtra("type", "search"));
            }
        };
        method = new Method(DietSearch.this, adShow);
        method.setStatusBarGradiant();

        Intent intent = getIntent();
        search = intent.getStringExtra("search");

        toolbar = findViewById(R.id.toolbar_search);
        toolbar.setTitle(search);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayout = findViewById(R.id.linearLayout_search);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, DietSearch.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, DietSearch.this);
        }

        progressBar = findViewById(R.id.progressbar_search);
        recyclerView = findViewById(R.id.recyclerView_search);
        textView = findViewById(R.id.textView_search);

        textView.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DietSearch.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (Method.isNetworkAvailable(DietSearch.this)) {
            SearchDetail();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }


    }

    public void SearchDetail() {

        progressBar.setVisibility(View.VISIBLE);

        Constant_Api.searchList.clear();

        String url = Constant_Api.search_diet + search;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String cid = object.getString("cat_id");
                        String diet_title = object.getString("diet_title");
                        String diet_info = object.getString("diet_info");
                        String diet_image = object.getString("diet_image");

                        Constant_Api.searchList.add(new SubCategoryList(id, cid, diet_title, diet_info, diet_image));

                    }
                    if (Constant_Api.searchList.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                        subCategoryAdapter = new SubCategoryAdapter(DietSearch.this, Constant_Api.searchList, adShow);
                        recyclerView.setAdapter(subCategoryAdapter);
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
